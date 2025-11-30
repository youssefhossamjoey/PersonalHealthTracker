import { useState, useEffect } from 'react'
import { useApi } from "../api/api";
import './RecipeGrid.css'
import RecipeCard from './RecipeCard';
import Select from "react-select";


export default function RecipeGrid() {
    const [recipes, setRecipes] = useState([]);
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')
    const [page, setPage] = useState(0)
    const [totalPages, setTotalPages] = useState(0)
    const [pageSize] = useState(20)
    const [searchTerm, setSearchTerm] = useState('')
    const [sortOption, setSortOption] = useState('')
    const [sortDirection, setSortDirection] = useState('asc')
    const [selectedIds, setSelectedIds] = useState(new Set())
    const [batchDeleteConfirm, setBatchDeleteConfirm] = useState(false)
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [availableFoodItems, setAvailableFoodItems] = useState([])
    const [recipeName, setRecipeName] = useState('')
    const [ingredients, setIngredients] = useState([]) // { foodItemId, amount }
    // const [formData, setFormData] = useState({ recipeName: '', kcal: '', protein: '' })
    const [submitError, setSubmitError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [deleteConfirm, setDeleteConfirm] = useState({ show: false, recipeId: null, recipeName: '' })
    const [showErrors, setShowErrors] = useState(false)
    const { api } = useApi();

    useEffect(() => {
        async function fetchRecipes() {
            try {
                setLoading(true)
                setError('')
                const searchParam = searchTerm ? `&search=${encodeURIComponent(searchTerm)}` : ''
                const sortParam = sortOption ? `&sort=${encodeURIComponent(sortOption + ',' + sortDirection)}` : ''
                const response = await api(`/api/recipe?page=${page}&size=${pageSize}${searchParam}${sortParam}`);
                if (!response) {
                    throw new Error(`Failed to fetch recipes: ${JSON.stringify(response)}`)
                }
                const data = await response
                setRecipes(data.content || [])
                setTotalPages(data.totalPages || 0)
            } catch (err) {
                console.error('Error fetching recipes:', err)
                setError(err.message || 'Failed to load recipes')
            } finally {
                setLoading(false)
            }
        }

        fetchRecipes()
    }, [page, pageSize, searchTerm, sortOption, sortDirection])



    const handleDelete = async (recipeId) => {
        let idsArray2 = [recipeId];
        try {
            await api('/api/recipe', {
                method: 'DELETE',
                body: JSON.stringify(idsArray2)
            })

            setDeleteConfirm({ show: false, recipeId: null, recipeName: '' })

            const refreshSearch = searchTerm ? `&search=${encodeURIComponent(searchTerm)}` : ''
            const refreshSort = sortOption ? `&sort=${encodeURIComponent(sortOption + ',' + sortDirection)}` : ''
            const refreshResponse = await api(`/api/recipe?page=${page}&size=${pageSize}${refreshSearch}${refreshSort}`)
            if (refreshResponse) {
                setRecipes(refreshResponse.content || [])
                setTotalPages(refreshResponse.totalPages || 0)

                if (refreshResponse.content.length === 0 && page > 0) {
                    setPage(page - 1)
                }
            }
        } catch (err) {
            console.error('Error deleting recipe:', err)
            alert('Failed to delete recipe. Please try again.')
        }
    }

    const toggleSelect = (id) => {
        setSelectedIds(prev => {
            const next = new Set(prev)
            if (next.has(id)) next.delete(id)
            else next.add(id)
            return next
        })
    }

    const selectAllOnPage = () => {
        setSelectedIds(prev => {
            const next = new Set(prev)
            recipes.forEach(it => next.add(it.id))
            return next
        })
    }

    // Load food items when opening create modal
    useEffect(() => {
        if (!isModalOpen) return
        async function loadFoodItems() {
            try {
                const resp = await api('/api/fooditem?page=0&size=200')
                const list = resp?.content || []
                setAvailableFoodItems(list)
                if (list.length > 0 && ingredients.length === 0) {
                    setIngredients([{ foodItemId: list[0].id, amount: '' }])
                }
            } catch (err) {
                console.error('Failed to load food items for recipe creation', err)
                setAvailableFoodItems([])
            }
        }
        loadFoodItems()
    }, [isModalOpen])

    const closeModal = () => {
        setIsModalOpen(false)
        setRecipeName('')
        setIngredients([])
        setSubmitError('')
        setShowErrors(false)
    }

    const addIngredientRow = () => {
        // pick first food item that's not already used
        const used = new Set(ingredients.map(i => i.foodItemId))
        const candidate = availableFoodItems.find(fi => fi.id && !used.has(fi.id))
        if (!candidate) {
            setSubmitError('All available food items have been added')
            return
        }
        setIngredients(prev => [...prev, { foodItemId: candidate.id, amount: '' }])
        setSubmitError('')
        setShowErrors(false)
    }

    const updateIngredient = (index, field, value) => {
        // prevent selecting the same food item twice
        if (field === 'foodItemId') {
            const already = ingredients.some((r, i) => i !== index && r.foodItemId === value)
            if (already) {
                setSubmitError('This ingredient has already been added')
                return
            }
            setSubmitError('')
        }

        setIngredients(prev => {
            const next = prev.map((r, i) => i === index ? { ...r, [field]: value } : r)
            return next
        })
        setShowErrors(false)
    }

    const getIngredientRowErrors = (ing, idx) => {
        let foodItemError = ''
        let amountError = ''

        if (!ing.foodItemId) {
            foodItemError = 'Select a food item'
        } else {
            // duplicate check
            const dup = ingredients.some((r, i) => i !== idx && r.foodItemId === ing.foodItemId)
            if (dup) foodItemError = 'This item is already added'
        }

        if (ing.amount === '' || Number.isNaN(parseFloat(ing.amount))) {
            amountError = 'Enter a valid amount'
        }

        return { foodItemError, amountError }
    }

    const removeIngredientRow = (index) => {
        setIngredients(prev => prev.filter((_, i) => i !== index))
    }

    const handleCreateRecipe = async (e) => {
        e.preventDefault()
        // validate synchronously before sending
        setShowErrors(true)
        setSubmitError('')
        if (!recipeName.trim()) { setSubmitError('Recipe name is required'); return }
        if (ingredients.length === 0) { setSubmitError('Add at least one ingredient'); return }
        // validate rows
        for (let i = 0; i < ingredients.length; i++) {
            const { foodItemError, amountError } = getIngredientRowErrors(ingredients[i], i)
            if (foodItemError || amountError) {
                setSubmitError('Please fix validation errors in ingredients')
                return
            }
        }

        const ingredientsObj = {}
        ingredients.forEach(it => {
            if (it.foodItemId) {
                ingredientsObj[it.foodItemId] = parseFloat(it.amount) || 0
            }
        })

        const payload = {
            name: recipeName,
            items: ingredientsObj
        }

        setIsSubmitting(true)
        try {
            await api('/api/recipe', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })

            // refresh list
            setPage(0)
            const refreshSearch = searchTerm ? `&search=${encodeURIComponent(searchTerm)}` : ''
            const refreshSort = sortOption ? `&sort=${encodeURIComponent(sortOption + ',' + sortDirection)}` : ''
            const refreshResponse = await api(`/api/recipe?page=0&size=${pageSize}${refreshSearch}${refreshSort}`)
            if (refreshResponse) {
                setRecipes(refreshResponse.content || [])
                setTotalPages(refreshResponse.totalPages || 0)
            }

            closeModal()
        } catch (err) {
            console.error('Error creating recipe', err)
            setSubmitError(err.message || 'Failed to create recipe')
        } finally {
            setIsSubmitting(false)
        }
    }

    const clearSelection = () => setSelectedIds(new Set())

    const handleBatchDelete = () => {
        if (selectedIds.size === 0) return
        setBatchDeleteConfirm(true)
    }

    const confirmBatchDelete = async () => {
        try {
            const idsArray = Array.from(selectedIds)
            await api('/api/recipe', {
                method: 'DELETE',
                body: JSON.stringify(idsArray)
            })

            // Clear selection and refresh grid
            clearSelection()
            setBatchDeleteConfirm(false)
            const refreshSearch = searchTerm ? `&search=${encodeURIComponent(searchTerm)}` : ''
            const refreshSort = sortOption ? `&sort=${encodeURIComponent(sortOption)}` : ''
            const refreshResponse = await api(`/api/recipe?page=${page}&size=${pageSize}${refreshSearch}${refreshSort}`)
            if (refreshResponse) {
                setRecipes(refreshResponse.content || [])
                setTotalPages(refreshResponse.totalPages || 0)
                if (refreshResponse.content.length === 0 && page > 0) setPage(page - 1)
            }
        } catch (err) {
            console.error('Batch delete failed', err)
            alert('Failed to delete selected recipes.')
            setBatchDeleteConfirm(false)
        }
    }

    const showDeleteConfirm = (recipeId, recipeName) => {
        setDeleteConfirm({ show: true, recipeId, recipeName })
    }

    const cancelDelete = () => {
        setDeleteConfirm({ show: false, recipeId: null, recipeName: '' })
    }

    // disable create when name missing or any ingredient is incomplete
    const isCreateDisabled = isSubmitting || !recipeName.trim() || ingredients.length === 0 || ingredients.some(it => !it.foodItemId || it.amount === '' || Number.isNaN(parseFloat(it.amount)))


    return (
        <div className="recipe-grid-container">
            {error && <div className="recipe-grid-warning">Using mock data: {error}</div>}

            <div className="recipe-grid-controls">
                <div className="recipe-grid-search">
                    <input
                        type="text"
                        placeholder="Search recipes..."
                        className="search-input"
                        value={searchTerm}
                        onChange={(e) => { setSearchTerm(e.target.value); setPage(0); }}
                    />
                </div>

                <div className="recipe-grid-actions">
                    <select className="sort-select" value={sortOption} onChange={(e) => { setSortOption(e.target.value); setPage(0); }}>
                        <option value="">Sort by...</option>
                        <option value="name">Name</option>
                        <option value="kcal">Calories</option>
                        <option value="pro">Protein</option>
                    </select>

                    <button
                        className="sort-dir-btn"
                        title={`Sort direction: ${sortDirection}`}
                        onClick={() => { setSortDirection(prev => prev === 'asc' ? 'desc' : 'asc'); setPage(0); }}
                    >
                        {sortDirection === 'asc' ? '▲' : '▼'}
                    </button>

                    <button className="create-btn" onClick={() => setIsModalOpen(true)}>
                        + Create Recipe
                    </button>

                    <button className="select-all-btn" onClick={selectAllOnPage} title="Select all visible">
                        Select all
                    </button>

                    <button className="batch-delete-btn" onClick={handleBatchDelete} disabled={selectedIds.size === 0}>
                        Delete Selected ({selectedIds.size})
                    </button>
                </div>
            </div>

            {isModalOpen && (
                <div className="modal-overlay" onClick={closeModal}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2>Create New Recipe</h2>
                            <button className="modal-close" onClick={closeModal}>
                                ×
                            </button>
                        </div>
                        <form className="modal-form" onSubmit={handleCreateRecipe}>
                            {submitError && <div className="form-error">{submitError}</div>}

                            <div className="form-group">
                                <label htmlFor="recipeName">Recipe Name *</label>
                                <input
                                    type="text"
                                    id="recipeName"
                                    value={recipeName}
                                    onChange={(e) => setRecipeName(e.target.value)}
                                    placeholder="e.g., Chicken Salad"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <div className="form-group-header">
                                    <label>Ingredients</label>
                                    <button type="button" className="btn-add" onClick={addIngredientRow}>+ Add Ingredient</button>
                                </div>
                                {ingredients.map((ing, idx) => {
                                    const { foodItemError, amountError } = getIngredientRowErrors(ing, idx)
                                    return (
                                        <div key={idx} className="ingredient-row">
                                            <div style={{ flex: '1 1 auto' }}>
                                                <Select
                                                    classNamePrefix="foodselect"
                                                    className={`form-input-food-item ${foodItemError ? 'input-invalid' : ''}`}
                                                    value={
                                                        availableFoodItems
                                                            .map(fi => ({
                                                                value: fi.id,
                                                                label: fi.name,
                                                                fi
                                                            }))
                                                            .find(opt => opt.value === ing.foodItemId) || null
                                                    }
                                                    onChange={(option) => updateIngredient(idx, 'foodItemId', option.value)}
                                                    options={availableFoodItems.map(fi => ({
                                                        value: fi.id,
                                                        label: fi.name,
                                                        fi, // attach full item for custom rendering
                                                        isDisabled: ingredients.some(
                                                            (r, i) => i !== idx && r.foodItemId === fi.id
                                                        )
                                                    }))}
                                                    isClearable
                                                    isSearchable
                                                    getOptionLabel={(opt) => (
                                                        <div style={{ display: "flex", justifyContent: "space-between", width: "100%" }}>
                                                            <span>{opt.label}</span>
                                                            <span style={{ opacity: 0.6, fontSize: "0.8rem" }}>
                                                                {opt.fi.kcal != null ? `${opt.fi.kcal} kcal` : ""}
                                                                {opt.fi.pro != null ? ` • ${opt.fi.pro}g protein` : ""}
                                                            </span>
                                                        </div>
                                                    )}
                                                    getOptionValue={(opt) => opt.value}
                                                />
                                                {showErrors && foodItemError && (
                                                    <div className="input-error-text">{foodItemError}</div>
                                                )}
                                            </div>

                                            <div style={{ width: '140px' }}>
                                                <input
                                                    className={`form-input-amount ${amountError ? 'input-invalid' : ''}`}
                                                    type="number"
                                                    placeholder="amount"
                                                    value={ing.amount}
                                                    onChange={(e) => updateIngredient(idx, 'amount', e.target.value)}
                                                    step="0.1"
                                                    min="0"
                                                    required
                                                />
                                                {showErrors && amountError && (
                                                    <div className="input-error-text">{amountError}</div>
                                                )}
                                            </div>

                                            <button type="button" className="btn-remove" onClick={() => removeIngredientRow(idx)}>×</button>
                                        </div>
                                    )
                                })}
                                <div className="form-actions-row">

                                </div>
                            </div>

                            <div className="modal-actions">
                                <button
                                    type="button"
                                    className="btn-cancel"
                                    onClick={closeModal}
                                    disabled={isSubmitting}
                                >
                                    Cancel
                                </button>
                                <button type="submit" className="btn-submit" disabled={isCreateDisabled}>
                                    {isSubmitting ? 'Creating...' : 'Create Recipe'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {loading && <div className="recipe-grid-loading">Loading recipes...</div>}

            {!loading && error && recipes.length === 0 && (
                <div className="recipe-grid-error">Error: {error}</div>
            )}

            {!loading && recipes.length === 0 && !error && (
                <div className="recipe-grid-empty">No recipes found. Add your first recipe!</div>
            )}

            {!loading && recipes.length > 0 && (
                <div className="recipe-grid">
                    {recipes.map((recipe) => (
                        <div key={recipe.id} className="recipe-grid-row">
                            <RecipeCard
                                recipe={recipe}
                                onDelete={showDeleteConfirm}
                                selected={selectedIds.has(recipe.id)}
                                onToggleSelect={() => toggleSelect(recipe.id)}
                            />
                        </div>
                    ))}
                </div>
            )}

            {deleteConfirm.show && (
                <div className="modal-overlay" onClick={cancelDelete}>
                    <div className="confirm-modal" onClick={(e) => e.stopPropagation()}>
                        <div className="confirm-icon">⚠️</div>
                        <h3 className="confirm-title">Delete Recipe?</h3>
                        <p className="confirm-message">
                            Are you sure you want to delete "<strong>{deleteConfirm.recipeName}</strong>"?
                            This action cannot be undone.
                        </p>
                        <div className="confirm-actions">
                            <button className="btn-confirm-cancel" onClick={cancelDelete}>
                                Cancel
                            </button>
                            <button
                                className="btn-confirm-delete"
                                onClick={() => handleDelete(deleteConfirm.recipeId)}
                            >
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {batchDeleteConfirm && (
                <div className="modal-overlay" onClick={() => setBatchDeleteConfirm(false)}>
                    <div className="confirm-modal" onClick={(e) => e.stopPropagation()}>
                        <div className="confirm-icon">⚠️</div>
                        <h3 className="confirm-title">Delete Selected Recipes?</h3>
                        <p className="confirm-message">
                            Are you sure you want to delete <strong>{selectedIds.size}</strong> selected recipe(s)? This action cannot be undone.
                        </p>
                        <div className="confirm-actions">
                            <button className="btn-confirm-cancel" onClick={() => setBatchDeleteConfirm(false)}>
                                Cancel
                            </button>
                            <button
                                className="btn-confirm-delete"
                                onClick={confirmBatchDelete}
                            >
                                Delete Selected
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {totalPages > 1 && (
                <div className="recipe-grid-pagination">
                    <button
                        onClick={() => setPage((p) => Math.max(0, p - 1))}
                        disabled={page === 0}
                        className="pagination-btn"
                    >
                        Previous
                    </button>
                    <span className="pagination-info">
                        Page {page + 1} of {totalPages}
                    </span>
                    <button
                        onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                        disabled={page >= totalPages - 1}
                        className="pagination-btn"
                    >
                        Next
                    </button>
                </div>
            )}
        </div>
    )
}
import { useState, useEffect } from 'react'
import ItemCard from './ItemCard'
import { useApi } from "../api/api";
import './ItemGrid.css'

export default function ItemGrid() {
    const [items, setItems] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')
    const [page, setPage] = useState(0)
    const [totalPages, setTotalPages] = useState(0)
    const [pageSize] = useState(20)
    const [searchTerm, setSearchTerm] = useState('')
    const [sortOption, setSortOption] = useState('')
    const [selectedIds, setSelectedIds] = useState(new Set())
    const [batchDeleteConfirm, setBatchDeleteConfirm] = useState(false)
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [formData, setFormData] = useState({ itemName: '', kcal: '', protein: '' })
    const [submitError, setSubmitError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [deleteConfirm, setDeleteConfirm] = useState({ show: false, itemId: null, itemName: '' })
    const { api } = useApi();

    useEffect(() => {
        async function fetchItems() {
            try {
                setLoading(true)
                setError('')
                // Build query params: page, size, optional search and sort
                const searchParam = searchTerm ? `&search=${encodeURIComponent(searchTerm)}` : ''
                const sortParam = sortOption ? `&sort=${encodeURIComponent(sortOption)}` : ''
                const response = await api(`/api/fooditem?page=${page}&size=${pageSize}${searchParam}${sortParam}`);
                if (!response) {
                    throw new Error(`Failed to fetch items: ${JSON.stringify(response)}`)
                }
                const data = await response
                setItems(data.content || [])
                setTotalPages(data.totalPages || 0)
            } catch (err) {
                console.error('Error fetching items:', err)
                setError(err.message || 'Failed to load items')
            } finally {
                setLoading(false)
            }
        }

        fetchItems()
    }, [page, pageSize, searchTerm, sortOption])

    const handleInputChange = (e) => {
        const { id, value } = e.target
        setFormData(prev => ({ ...prev, [id]: value }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setSubmitError('')
        setIsSubmitting(true)

        try {
            const payload = {
                name: formData.itemName,
                kcal: parseFloat(formData.kcal),
                pro: parseFloat(formData.protein)
            }

            try {
                await api('/api/fooditem', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                })
            } catch {
                throw new Error('Failed to create item')
            }

            // Reset form and close modal
            setFormData({ itemName: '', kcal: '', protein: '' })
            setIsModalOpen(false)

            // Refresh the grid - go back to first page to see new item
            setPage(0)

            // Refetch items (respect current search and sort)
            const refreshSearch = searchTerm ? `&search=${encodeURIComponent(searchTerm)}` : ''
            const refreshSort = sortOption ? `&sort=${encodeURIComponent(sortOption)}` : ''
            const refreshResponse = await api(`/api/fooditem?page=0&size=${pageSize}${refreshSearch}${refreshSort}`)
            if (refreshResponse) {
                setItems(refreshResponse.content || [])
                setTotalPages(refreshResponse.totalPages || 0)
            }
        } catch (err) {
            console.error('Error creating item:', err)
            setSubmitError(err.message || 'Failed to create item')
        } finally {
            setIsSubmitting(false)
        }
    }

    const closeModal = () => {
        setIsModalOpen(false)
        setFormData({ itemName: '', kcal: '', protein: '' })
        setSubmitError('')
    }

    const handleDelete = async (itemId) => {
        try {
            await api(`/api/fooditem/${itemId}`, {
                method: 'DELETE'
            })

            // Close confirm modal
            setDeleteConfirm({ show: false, itemId: null, itemName: '' })

            // Refetch items to update the grid (respect current search and sort)
            const refreshSearch = searchTerm ? `&search=${encodeURIComponent(searchTerm)}` : ''
            const refreshSort = sortOption ? `&sort=${encodeURIComponent(sortOption)}` : ''
            const refreshResponse = await api(`/api/fooditem?page=${page}&size=${pageSize}${refreshSearch}${refreshSort}`)
            if (refreshResponse) {
                setItems(refreshResponse.content || [])
                setTotalPages(refreshResponse.totalPages || 0)

                // If current page is now empty and not the first page, go back one page
                if (refreshResponse.content.length === 0 && page > 0) {
                    setPage(page - 1)
                }
            }
        } catch (err) {
            console.error('Error deleting item:', err)
            alert('Failed to delete item. Please try again.')
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
            items.forEach(it => next.add(it.id))
            return next
        })
    }

    const clearSelection = () => setSelectedIds(new Set())

    const handleBatchDelete = () => {
        if (selectedIds.size === 0) return
        setBatchDeleteConfirm(true)
    }

    const confirmBatchDelete = async () => {
        try {
            const idsArray = Array.from(selectedIds)
            await api('/api/fooditem', {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(idsArray)
            })

            // Clear selection and refresh grid
            clearSelection()
            setBatchDeleteConfirm(false)
            const refreshSearch = searchTerm ? `&search=${encodeURIComponent(searchTerm)}` : ''
            const refreshSort = sortOption ? `&sort=${encodeURIComponent(sortOption)}` : ''
            const refreshResponse = await api(`/api/fooditem?page=${page}&size=${pageSize}${refreshSearch}${refreshSort}`)
            if (refreshResponse) {
                setItems(refreshResponse.content || [])
                setTotalPages(refreshResponse.totalPages || 0)
                if (refreshResponse.content.length === 0 && page > 0) setPage(page - 1)
            }
        } catch (err) {
            console.error('Batch delete failed', err)
            alert('Failed to delete selected items.')
            setBatchDeleteConfirm(false)
        }
    }

    const showDeleteConfirm = (itemId, itemName) => {
        setDeleteConfirm({ show: true, itemId, itemName })
    }

    const cancelDelete = () => {
        setDeleteConfirm({ show: false, itemId: null, itemName: '' })
    }

    return (
        <div className="item-grid-container">
            {error && <div className="item-grid-warning">Using mock data: {error}</div>}

            <div className="item-grid-controls">
                <div className="item-grid-search">
                    <input
                        type="text"
                        placeholder="Search items..."
                        className="search-input"
                        value={searchTerm}
                        onChange={(e) => { setSearchTerm(e.target.value); setPage(0); }}
                    />
                </div>

                <div className="item-grid-actions">
                    <select className="sort-select" value={sortOption} onChange={(e) => { setSortOption(e.target.value); setPage(0); }}>
                        <option value="">Sort by...</option>
                        <option value="name">Name</option>
                        <option value="kcal">Calories</option>
                        <option value="protein">Protein</option>
                    </select>

                    <button className="create-btn" onClick={() => setIsModalOpen(true)}>
                        + Create Item
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
                            <h2>Create New Item</h2>
                            <button className="modal-close" onClick={closeModal}>
                                ×
                            </button>
                        </div>
                        <form className="modal-form" onSubmit={handleSubmit}>
                            {submitError && <div className="form-error">{submitError}</div>}
                            <div className="form-group">
                                <label htmlFor="itemName">Item Name *</label>
                                <input
                                    type="text"
                                    id="itemName"
                                    value={formData.itemName}
                                    onChange={handleInputChange}
                                    placeholder="e.g., Apple, Chicken Breast"
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="kcal">Calories (kcal) *</label>
                                <input
                                    type="number"
                                    id="kcal"
                                    value={formData.kcal}
                                    onChange={handleInputChange}
                                    placeholder="e.g., 52"
                                    step="0.1"
                                    min="0"
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="protein">Protein (g) *</label>
                                <input
                                    type="number"
                                    id="protein"
                                    value={formData.protein}
                                    onChange={handleInputChange}
                                    placeholder="e.g., 0.3"
                                    step="0.1"
                                    min="0"
                                    required
                                />
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
                                <button type="submit" className="btn-submit" disabled={isSubmitting}>
                                    {isSubmitting ? 'Creating...' : 'Create Item'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {loading && <div className="item-grid-loading">Loading items...</div>}

            {!loading && error && items.length === 0 && (
                <div className="item-grid-error">Error: {error}</div>
            )}

            {!loading && items.length === 0 && !error && (
                <div className="item-grid-empty">No items found. Add your first item!</div>
            )}

            {!loading && items.length > 0 && (
                <div className="item-grid">
                    {items.map((item) => (
                        <ItemCard
                            key={item.id}
                            item={item}
                            onDelete={showDeleteConfirm}
                            selected={selectedIds.has(item.id)}
                            onToggleSelect={() => toggleSelect(item.id)}
                        />
                    ))}
                </div>
            )}

            {deleteConfirm.show && (
                <div className="modal-overlay" onClick={cancelDelete}>
                    <div className="confirm-modal" onClick={(e) => e.stopPropagation()}>
                        <div className="confirm-icon">⚠️</div>
                        <h3 className="confirm-title">Delete Item?</h3>
                        <p className="confirm-message">
                            Are you sure you want to delete "<strong>{deleteConfirm.itemName}</strong>"?
                            This action cannot be undone.
                        </p>
                        <div className="confirm-actions">
                            <button className="btn-confirm-cancel" onClick={cancelDelete}>
                                Cancel
                            </button>
                            <button
                                className="btn-confirm-delete"
                                onClick={() => handleDelete(deleteConfirm.itemId)}
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
                        <h3 className="confirm-title">Delete Selected Items?</h3>
                        <p className="confirm-message">
                            Are you sure you want to delete <strong>{selectedIds.size}</strong> selected item(s)? This action cannot be undone.
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
                <div className="item-grid-pagination">
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

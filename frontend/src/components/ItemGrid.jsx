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
                // Use paginated endpoint with page and size params
                const response = await api(`/api/fooditem?page=${page}&size=${pageSize}`);
                if (!response) {
                    throw new Error(`Failed to fetch items: ${JSON.stringify(response)}`)
                }
                const data = await response
                // Spring Boot paginated response structure
                setItems(data.content || [])
                setTotalPages(data.totalPages || 0)
            } catch (err) {
                console.error('Error fetching items:', err)
                setError(err.message || 'Failed to load items')
                // Mock paginated data for development
                setItems([
                    { id: 1, itemName: 'Apple', kcal: 52, protein: 0.3 },
                    { id: 2, itemName: 'Banana', kcal: 89, protein: 1.1 },
                    { id: 3, itemName: 'Chicken Breast', kcal: 165, protein: 31 },
                ])
                setTotalPages(1)
            } finally {
                setLoading(false)
            }
        }

        fetchItems()
    }, [page, pageSize])

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
                const response = await api('/api/fooditem', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                })
            }
            catch {

                throw new Error('Failed to create item')
            }

            // Reset form and close modal
            setFormData({ itemName: '', kcal: '', protein: '' })
            setIsModalOpen(false)

            // Refresh the grid - go back to first page to see new item
            setPage(0)

            // Refetch items
            const refreshResponse = await api(`/api/fooditem?page=0&size=${pageSize}`)
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

            // Refetch items to update the grid
            const refreshResponse = await api(`/api/fooditem?page=${page}&size=${pageSize}`)
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
                    />
                </div>

                <div className="item-grid-actions">
                    <select className="sort-select">
                        <option value="">Sort by...</option>
                        <option value="name">Name</option>
                        <option value="kcal">Calories</option>
                        <option value="protein">Protein</option>
                    </select>

                    <button className="create-btn" onClick={() => setIsModalOpen(true)}>
                        + Create Item
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
                        <ItemCard key={item.id} item={item} onDelete={showDeleteConfirm} />
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

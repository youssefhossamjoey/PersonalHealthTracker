import PropTypes from 'prop-types'
import './ItemCard.css'

export default function ItemCard({ item, onDelete, selected = false, onToggleSelect }) {
  const handleDelete = () => {
    onDelete(item.id, item.name)
  }

  return (
    <div className="item-card">
      {typeof onToggleSelect === 'function' && (
        <label className="item-card-select">
          <input type="checkbox" checked={!!selected} onChange={onToggleSelect} />
        </label>
      )}

      <button className="item-card-delete" onClick={handleDelete} title="Delete item">
        ×
      </button>
      <h3 className="item-card-name">{item.name}</h3>
      <div className="item-card-details">
        <div className="item-card-stat">
          <span className="item-card-label">Calories</span>
          <span className="item-card-value">{item.kcal ? `${item.kcal} kcal` : 'N/A'}</span>
        </div>
        <div className="item-card-stat">
          <span className="item-card-label">Protein</span>
          <span className="item-card-value">{item.pro ? `${item.pro}g` : 'N/A'}</span>
        </div>
      </div>
    </div>
  )
}

ItemCard.propTypes = {
  item: PropTypes.shape({
    id: PropTypes.string,
    name: PropTypes.string.isRequired,
    kcal: PropTypes.number,
    protein: PropTypes.number,
  }).isRequired,
  onDelete: PropTypes.func.isRequired,
  selected: PropTypes.bool,
  onToggleSelect: PropTypes.func,
}

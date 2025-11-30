import PropTypes from 'prop-types'
import './RecipeCard.css'

export default function RecipeCard({ recipe, onDelete, selected = false, onToggleSelect }) {
  const handleDelete = () => {
    onDelete(recipe.id, recipe.name)
  }

  return (
    <div className="recipe-card">
      {typeof onToggleSelect === 'function' && (
        <label className="recipe-card-select">
          <input type="checkbox" checked={!!selected} onChange={onToggleSelect} />
        </label>
      )}

      <button className="recipe-card-delete" onClick={handleDelete} title="Delete recipe">
        ×
      </button>
      <h3 className="recipe-card-name">{recipe.name}</h3>
      <div className="recipe-card-details">
        <div className="recipe-card-stat">
          <span className="recipe-card-label">Calories</span>
          <span className="recipe-card-value">{recipe.kcal ? `${recipe.kcal} kcal` : 'N/A'}</span>
        </div>
        <div className="recipe-card-stat">
          <span className="recipe-card-label">Protein</span>
          <span className="recipe-card-value">{recipe.pro ? `${recipe.pro}g` : 'N/A'}</span>
        </div>
      </div>
    </div>
  )
}

RecipeCard.propTypes = {
  recipe: PropTypes.shape({
    id: PropTypes.string,
    name: PropTypes.string.isRequired,
    kcal: PropTypes.number,
    protein: PropTypes.number,
  }).isRequired,
  onDelete: PropTypes.func.isRequired,
  selected: PropTypes.bool,
  onToggleSelect: PropTypes.func,
}

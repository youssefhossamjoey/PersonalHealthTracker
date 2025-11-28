import { Box, Button } from '@mui/material';
import RestaurantIcon from '@mui/icons-material/Restaurant';
import { Link, useLocation } from 'react-router-dom';
import './NavBar.css';

const NavBar = () => {
    const location = useLocation();
    
    return (
        <Box className="pht-sidebar">
            <Button
                component={Link}
                to="/items"
                variant="contained"
                startIcon={<RestaurantIcon />}
                disableRipple
                disableElevation
                className={`nav-button ${location.pathname === '/items' ? 'nav-button-active' : ''}`}
            >
                Ingredient
            </Button>
            <Button
                variant="contained"
                startIcon={<RestaurantIcon />}
                disableRipple
                disableElevation
                className="nav-button"
            >
                Ingredient
            </Button>
            <Button
                variant="contained"
                startIcon={<RestaurantIcon />}
                disableRipple
                disableElevation
                className="nav-button"
            >
                Ingredient
            </Button>
        </Box>
    );
};

export default NavBar;
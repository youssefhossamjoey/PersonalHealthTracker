import { Box, Button } from '@mui/material';
import RestaurantIcon from '@mui/icons-material/Restaurant';
import './NavBar.css';

const NavBar = () => {
    return (
        <Box className="pht-sidebar">
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
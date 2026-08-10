import { useState } from 'react';
import { Box, Button, IconButton, Tooltip } from '@mui/material';
import RestaurantIcon from '@mui/icons-material/Restaurant';
import MenuBookIcon from '@mui/icons-material/MenuBook';
import DinnerDiningIcon from '@mui/icons-material/DinnerDining';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import { Link, useLocation } from 'react-router-dom';
import './NavBar.css';

const NAV_ITEMS = [
    { to: '/items', label: 'Ingredient', icon: <RestaurantIcon /> },
    { to: '/recipes', label: 'Recipe', icon: <MenuBookIcon /> },
    { to: '/meals', label: 'Meal', icon: <DinnerDiningIcon /> },
];

const NavBar = () => {
    const location = useLocation();
    const [collapsed, setCollapsed] = useState(false);

    return (
        <Box className={`pht-sidebar ${collapsed ? 'pht-sidebar-collapsed' : ''}`}>
            <IconButton
                onClick={() => setCollapsed(!collapsed)}
                className="nav-toggle"
                disableRipple
                size="small"
                aria-label={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}
            >
                {collapsed ? <ChevronRightIcon /> : <ChevronLeftIcon />}
            </IconButton>

            <Box className="nav-links">
                {NAV_ITEMS.map(({ to, label, icon }) => {
                    const isActive = location.pathname === to;
                    const button = (
                        <Button
                            key={to}
                            component={Link}
                            to={to}
                            startIcon={collapsed ? null : icon}
                            disableRipple
                            disableElevation
                            className={`nav-button ${isActive ? 'nav-button-active' : ''}`}
                        >
                            {collapsed ? icon : label}
                        </Button>
                    );

                    return collapsed ? (
                        <Tooltip key={to} title={label} placement="right">
                            {button}
                        </Tooltip>
                    ) : (
                        button
                    );
                })}
            </Box>
        </Box>
    );
};

export default NavBar;
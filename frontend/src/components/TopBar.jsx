import { useState, useRef, useEffect } from 'react';
import {
    InputBase,
    IconButton,
    Avatar,
    Menu,
    MenuItem,
    ListItemIcon,
    Divider,
    Box,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import CloseIcon from '@mui/icons-material/Close';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import PersonOutlineIcon from '@mui/icons-material/PersonOutline';
import SettingsOutlinedIcon from '@mui/icons-material/SettingsOutlined';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext.jsx';
import './TopBar.css';

function TopBar() {
    const [anchorEl, setAnchorEl] = useState(null);
    const [search, setSearch] = useState('');
    const [searchOpen, setSearchOpen] = useState(false);
    const inputRef = useRef(null);
    const navigate = useNavigate();
    const { setAccessToken } = useAuth();

    const menuOpen = Boolean(anchorEl);

    const handleMenuOpen = (e) => setAnchorEl(e.currentTarget);
    const handleMenuClose = () => setAnchorEl(null);

    const handleLogout = () => {
        handleMenuClose();
        setAccessToken(null);
        navigate('/login');
    };

    const openSearch = () => setSearchOpen(true);

    const closeSearch = () => {
        setSearchOpen(false);
        setSearch('');
    };

    useEffect(() => {
        if (searchOpen && inputRef.current) {
            inputRef.current.focus();
        }
    }, [searchOpen]);

    return (
        <div className="pht-topbar">
            <div className="pht-topbar-left">
                <span className="pht-topbar-title">Personal Health Tracker</span>
            </div>

            <div className="pht-topbar-center">
                <Box className={`pht-search ${searchOpen ? 'pht-search-open' : ''}`}>
                    <IconButton
                        onClick={openSearch}
                        disableRipple
                        size="small"
                        className="pht-search-toggle"
                        aria-label="Open search"
                    >
                        <SearchIcon fontSize="small" />
                    </IconButton>

                    <InputBase
                        inputRef={inputRef}
                        placeholder="Search recipes, ingredients, meals…"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        onBlur={() => {
                            if (!search) closeSearch();
                        }}
                        className="pht-search-input"
                        inputProps={{ 'aria-label': 'search' }}
                    />

                    {searchOpen && (
                        <IconButton
                            onClick={closeSearch}
                            disableRipple
                            size="small"
                            className="pht-search-close"
                            aria-label="Close search"
                        >
                            <CloseIcon fontSize="small" />
                        </IconButton>
                    )}
                </Box>
            </div>

            <div className="pht-topbar-right">
                <IconButton className="pht-icon-btn" disableRipple size="small">
                    <NotificationsNoneIcon fontSize="small" />
                </IconButton>

                <IconButton onClick={handleMenuOpen} disableRipple size="small" className="pht-avatar-btn">
                    <Avatar className="pht-avatar">U</Avatar>
                </IconButton>

                <Menu
                    anchorEl={anchorEl}
                    open={menuOpen}
                    onClose={handleMenuClose}
                    anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                    transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                    slotProps={{ paper: { className: 'pht-user-menu' } }}
                >
                    <MenuItem onClick={handleMenuClose}>
                        <ListItemIcon>
                            <PersonOutlineIcon fontSize="small" />
                        </ListItemIcon>
                        Profile
                    </MenuItem>
                    <MenuItem onClick={handleMenuClose}>
                        <ListItemIcon>
                            <SettingsOutlinedIcon fontSize="small" />
                        </ListItemIcon>
                        Settings
                    </MenuItem>
                    <Divider />
                    <MenuItem onClick={handleLogout}>
                        <ListItemIcon>
                            <LogoutIcon fontSize="small" />
                        </ListItemIcon>
                        Log out
                    </MenuItem>
                </Menu>
            </div>
        </div>
    );
}

export default TopBar;
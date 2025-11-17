const USER_STORAGE_KEY = 'jwtToken';
const USER_NAME = 'username';
const USER_ID = 'userId';
const USER_ROLE = 'userRole';


export const storeUserSession = (token, name, id, role) => {
    try {
        sessionStorage.setItem(USER_STORAGE_KEY, token);
        sessionStorage.setItem(USER_NAME, name);
        sessionStorage.setItem(USER_ID, id);
        sessionStorage.setItem(USER_ROLE, role);
        console.log('User session data stored successfully');
    } catch (error) {
        console.error('Error storing user data in session storage:', error);
    }
};


export const getUserToken = () => {
    try {
        const userToken = sessionStorage.getItem(USER_STORAGE_KEY);
        return userToken || null;

    } catch (error) {
        console.error('Error retrieving token from session storage:', error);
        return null;
    }
};

export const getUserName = () => {
    try {
        const userName = sessionStorage.getItem(USER_NAME);
        return userName||null;

    } catch (error) {
        console.error('Error retrieving user name from session storage:', error);
        return null;
    }
};

export const getUserId = () => {
    try {
        const userId = sessionStorage.getItem(USER_ID);
        return userId || null;

    } catch (error) {
        console.error('Error retrieving user name from session storage:', error);
        return null;
    }
};

export const getUserRole = () => {
    try {
        const userRole = sessionStorage.getItem(USER_ROLE);
        return userRole;

    } catch (error) {
        console.error('Error retrieving user name from session storage:', error);
        return null;
    }
};


export const clearUserSession = () => {
    try {
        sessionStorage.removeItem(USER_STORAGE_KEY);
        sessionStorage.removeItem(USER_NAME);
        sessionStorage.removeItem(USER_ID);
        sessionStorage.removeItem(USER_ROLE);
        console.log('User session cleared.');
    } catch (error) {
        console.error('Error clearing user data from session storage:', error);
    }
};


export const isUserLoggedIn = () => {
    return !!sessionStorage.getItem(USER_STORAGE_KEY);
};
import apiClient from "../api";
import toast from "react-hot-toast";

export const handleDeleteUser = async (userId, onUserDeleted) => {
    if (window.confirm("Are you sure you want to delete this user? This action cannot be undone.")) {
        try {
            const response = await apiClient.delete(`/member/deleteuser/${userId}`);

            console.log(`User with id ${userId} successfully deleted.`);
            toast.success(`User deleted Successfully!`, { duration:4000 });

            onUserDeleted();
        } catch (error) {
            console.error("Error deleting user:", error);
            toast.error(`Error deleting user!`, { duration: 4000 });
        }
    }
};
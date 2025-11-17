import apiClient from "../api";

export default async function getTotalFineAmount(memberId) {
    
    const fineResponse = await apiClient.get(`/fine/getbymemberid/${memberId}`);
    const fines = fineResponse.data;

    const allDueFines = fines.filter((f) => {
      return f.status === "Due";
    });

    const totalDueFineAmount = allDueFines.reduce((total, fine) => {
        return total + (fine.amount || 0); 
    }, 0);

    return totalDueFineAmount;

}
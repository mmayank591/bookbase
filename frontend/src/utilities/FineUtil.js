import apiClient from "../api";

export async function fineHandler( memberId, transactionId, overdueDays ) {
  try {
   
    const finesResponse = await apiClient.get(`/fine/getbymemberid/${memberId}`);
    const allFines = finesResponse?.data ?? [];

    const existingFine = allFines.find(
      (f) => f.transactionId === transactionId && f.status === "Due"
    );

    
    const fineAmount = Math.round(20 * overdueDays);
    const payload = {
      memberID: memberId,
      transactionID: transactionId,
      amount: fineAmount,
      status: "Due",
    };

    const amount = {
        amount : fineAmount
    };

    
    if (existingFine) {
        
      const fineID = existingFine.fineID;
      console.log(`Updating existing DUE fine ${fineID} with amount ${fineAmount}`);
      const response = await apiClient.patch(`/fine/updateamount/${fineID}`, amount);
      
      console.log("Fine Updated!");
      console.log(response.data);

    } else {

      console.log(`Creating new fine for transaction ${transactionId}`);
      const response = await apiClient.post(`/fine/createnew`, payload);
      
      console.log("Fine Created!");
      console.log(response.data);
    }
  } catch (err) {
    console.error("Error in fineHandler:", err);
    return;
  }
}

public class PinChangeRequest {
	
	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getOldPinBlock() {
		return oldPinBlock;
	}

	public void setOldPinBlock(String oldPinBlock) {
		this.oldPinBlock = oldPinBlock;
	}

	public String getNewPinBlock() {
		return newPinBlock;
	}

	public void setNewPinBlock(String newPinBlock) {
		this.newPinBlock = newPinBlock;
	}

	private String cardno;
	private String oldPinBlock;
	private String newPinBlock;
	private String termNo;
	
	public String getTermNo() {
		return termNo;
	}

	public void setTermNo(String termNo) {
		this.termNo = termNo;
	}

	public PinChangeRequest(){
		
	}

}

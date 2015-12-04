
public class PinChange {
	public PinChangeResponse doPinChange(PinChangeRequest req){
		
		/*deal with the request message*/
		System.out.println("termId=[" + req.getTermNo() + "]");
		System.out.println("cardNo=[" + req.getCardno() + "]");
		System.out.println("oldPinBlock=[" + req.getOldPinBlock() + "]");
		System.out.println("newPinBlock=[" + req.getNewPinBlock() + "]");
		
		System.out.println("Assume the pin block is changed successfully");
		
		
		/*response message*/
		PinChangeResponse res=new PinChangeResponse();
		res.setRespCode("00");
		res.setReferenceNo("123456789012");
		res.setCardNo(req.getCardno());
		res.setTermNo(req.getTermNo());
		return res;		
	}

}

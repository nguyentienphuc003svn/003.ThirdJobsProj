package ccas;
public class CcasConf {
	//支付类型
  public static final int Pay_Type_CCAS = 0;       //结算宝交易
  public static final int Pay_Type_Direct = 1;        //即时付款交易

  //交易状态
  public static final String Pay_ST_PayWait = "0";       //等待买家付款
  public static final String Pay_ST_TradeCreated = "1";      //交易已创建，等待卖家确认
  public static final String Pay_ST_BankRemit = "2";    //支付宝确认买家银行汇款中，暂勿发货
  public static final String Pay_ST_PayDone = "3";     //买家已付款，等待卖家发货
  public static final String Pay_ST_GoodsSent = "4";   //卖家已发货，等待买家确认
  public static final String Pay_ST_GoodsReceived = "5"; //买家确认收货，等待支付宝打款给卖家
  public static final String Pay_ST_RefundApply = "6"; //申请退款
  public static final String Pay_ST_TradeEnd = "9";    //交易完成
  public static final String Pay_ST_TradeClosed = "A";  //交易关闭
  public static final String Pay_ST_TradCanceled = "B";  //交易取消
  public static final String Pay_ST_TradeRefused = "C"; //交易拒绝
  public static final String Pay_ST_TradeRefuseDeal = "D"; //交易拒绝处理
  
  
  
  }

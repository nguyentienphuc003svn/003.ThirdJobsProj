package ccasws;

import java.lang.reflect.*;
import org.springframework.transaction.*;
import javax.sql.DataSource;

public class CardInterface extends BaseInterface implements Cloneable,java.io.Serializable {

	private static final long serialVersionUID = 1L;

	//ÊôÐÔ

	private String cdno = "";
	private String cdnoa = "";
	private String cdno1 = "";
	private String cdno2 = "";
	private String cdno3 = "";
	private String cdno4 = "";
	private String cdno5 = "";
	private String cdno6 = "";
	private String cdno7 = "";
	private String cdno8 = "";
	private String cdno9 = "";
	private String cdno10= "";
	private String cdno11= "";
	private String cdno12= "";

	//C001
	private String idtp = "";

	private String idno = "";

	private String cuno = "";

	private String cust = "";
	private String cust1= "";
	private String cust2= "";

	private String tsbr = "";

	private String clnm = "";
	private String clnma= "";
	private String clnma1= "";

	private String addr = "";

	private String hadd = "";

	private String rmdf = "";

	private double mnsi = 0.00;

	private double mnoi = 0.00;

	private double mnti = 0.00;

	private String jbst = "";

	private String sxcd = "";

	private String natl = "";

	private String mrsu = "";

	//private	String	btdt=(new java.sql.Date(new java.util.Date().getTime())).toString();
	private String btdt = "";
	private String agnm = "";
	private String alnm = "";
	private String afnm = "";
	private String cutl = "";
	private String smmd = "";
	private String telv = "";
	private String eulv = "";

	private int fdcf = 0;

	private String fdlv = "";

	private String cuma = "";

	private String uscd = "";

	private String atpw = "";

	private String atpw1 = "";
	private String atpw2 = "";

	private String emad = "";

	private String tele = "";

	private String fxno = "";

	private String pscd = "";

	private String hdag = "";

	private String rgpl = "";

	private int rgpd = 0;

	private String rgno = "";

	private double rgfn = 0.00;

	private String rgdt = "";

	private String cmno = "";

	private String blgu = "";

	private String fpnm = "";

	private String fpno = "";

	private String fptl = "";

	private String monm = "";

	private String moid = "";

	private String motl = "";

	private String opno = "";

	private String lnca = "";

	private double spam = 0.00;

	private String txcd = "";

	private String txcd1 = "";

	private String opdt = "";

	private String opus = "";

	private String cldt = "";

	private String clus = "";

	// V001
	private String cdtp = "";

	private String cdtp1 = "";
	private String cdtp2 = "";
	private String cdtp3 = "";
	private String cdtp4 = "";

	private String cdtpa = "";


	private String ebno = "";

	private String ebnoa = "";

	private String inbr = "";

	private String cunoa = "";

	private String indt = "";

	private String inus = "";

	private String efdt = "";

	private String vlpd = "";

	private String ivdt = "";

	private String cvc1 = "";

	private String cvc2 = "";

	private String sucd = "";

	private String tspw = "";
	private String tspw1= "";
	private String tspw2= "";

	private int pera = 0;
	private int upra = 0;

	private int smrf = 0;

	private int smpg = 0;

	private int smln = 0;

	private String ltdt = "";

	private int ltrf = 0;

	private String ptcd = "";

	private String rldt = "";

	private String rlus = "";

	private String rlcd = "";

	private String cdst = "";

	private String cdst1 = "";

	private String cdsta = "";

	private String ffdt = "";

	private String ffus = "";

	private String spct = "";

	// V002
	private String acct = "";

	private String accta = "";

	//D001
	private String tsdt = "";

	private int tsrf = 0;
	private int tsrf1 = 0;
	private int tsrf2 = 0;
	private int tsrf3 = 0;
	private int tsrf4 = 0;
	private int tsrf5 = 0;
	private int tsrf6 = 0;
	private int tsrf7 = 0;
	private int tsrf8 = 0;
	private int pgno = 0;
	private int tstm = 0;
	private int tstm1= 0;
	private int tstm2= 0;
	private int tstm3= 0;
	private int tstm4= 0;
	private int tstm5= 0;
	private int tstm6= 0;
	private int tstm7= 0;
	private int tstm8= 0;
	private int tstm9= 0;

	private String wrtp = "";
	private String wrtpa= "";

	private String prid = "";
	private String prid1 = "";

	private String bstp = "";
	private String bstpa= "";

	private String fg11 = "";

	private String acct1 = "";

	private String acct2 = "";

	private String acit = "";

	private String acita = "";

	private String cyno = "";
	private String cyno1= "";
	private String cyno2= "";
	private String cyno3= "";
	private String cyno4= "";
	private String cyno5= "";
	private String cyno6= "";
	private String cyno7= "";
	private String cyno8= "";
	private String cyno9= "";
	private String cyno10= "";
	private String cyno11= "";
	private String cyno12= "";

	private String cynoa = "";

	private String cyfl = "";

	private double tsam = 0.00;

	private String tsamac = "";
	private String tsamac1= "";
	private String tsamac2= "";

	private double tsam1 = 0.00;
	private double tsam2 = 0.00;
	private double gdbl = 0.0;
	private double gcbl = 0.0;
	private double adbl = 0.0;

	private int num = 0;
	private int prnm= 0;
	private int drnu = 0;
	private int crnu = 0;
	private String numac = "";

	private String stno = "";

	private String stnoa = "";

	private String edno = "";

	private String ednoa = "";

	private String tscd = "";

	private String tsus = "";

	private String ckus = "";

	private String auus = "";

	private String tsst = "";

	private String rvdt = "";

	private int rvrf = 0;

	private String comm = "";

	//P014
	private String ercd = "";

	private String ermg = "";

	private String apmg = "";

	//D002
	private int vcrf = 0;
	private int vcrf1 = 0;

	private String acbr = "";

	private String ckbr = "";

	private String tsfm = "";

	private String otcd = "";
	private String otcd1= "";
	private String otcd2= "";

	private String amcd = "";
	private String amcd1= "";

	private String amcda = "";

	private String bldr = "";

	private double acbl = 0.00;
	private double acbl1= 0.00;
	private double acbl2= 0.00;
	private double acbl3= 0.00;
	private double acbl4= 0.00;
	private double acbl5= 0.00;
	private double acbl6= 0.00;
	private double acbl7= 0.00;
	private double acbl8= 0.00;

	private String acblac = "";

	private String toit = "";

	private double inat = 0.00;

	private String tsb1 = "";

	private String vcst = "";

	private String blno = "";
	private String blno1= "";
	private String blno2= "";

	private int srno = 0;
	private int srno1 = 0;
	private int srno2 = 0;
	private int srno3 = 0;

	private String anno = "";

	private String usno = "";


	private String idno1 = "";

	private String iofg = "";

	//D003
	private String svnt = "";

	private String pocd = "";
	private String pocd1= "";

	private String vldt = "";

	private String mudt = "";
	private String mudt1= "";
	private String mudt2= "";
	private String mudt3= "";
	private String mudt4= "";
	private String mudt5= "";
	private String mudt6= "";
	private String mudt7= "";
	private String mudt8= "";
	private String mudt9= "";
	private String mudt10= "";
	private String mudt11= "";
	private String mudt12= "";

	private String xddt = "";

	private String nxdt = "";

	private float inrd = 0;
	private float inrc = 0;

	private float inro = 0;

	private double drjs = 0.00;

	private double crjs = 0.00;

	private double ovjg = 0.00;

	private double paac = 0.00;

	private int tmdb = 0;

	private double amdb = 0.00;

	private int tmcr = 0;

	private double amcr = 0.00;

	private double icin = 0.00;

	private double pyin = 0.00;

	private double aiin = 0.00;

	private double apin = 0.00;

	private double pdam = 0.00;

	private double feam = 0.00;

	private double ram1 = 0.00;

	private double ram2 = 0.00;

	private double odam = 0.00;

	private String drpr = "";

	private double blpr = 0.00;

	private String updt = "";

	private String acst = "";
	private String acst1= "";
	private String acst2= "";
	private String acst3= "";
	private String acst4= "";

	private double ampa = 0.00;

	private String autr = "";

	private int anum = 0;

	private String frus = "";

	private String frdt = "";

	private String frcd = "";

	private String ckfg = "";

	private String flg1 = "";
	private String flg2 = "";
	private String flg3 = "";
	private String flg4 = "";
	private String flg5 = "";
	private String flg6 = "";
	private String flg7 = "";
	private String flg8 = "";
	private String flg9 = "";

	private int coym = 0;

	private int bnum = 0;

	private String iiac = "";

	//G001
	private String acdt = "";

	private String tatp = "";

	private String blat = "";

	private double dlbl = 0.00;

	private double clbl = 0.00;

	private double dtam = 0.00;

	private double cram = 0.00;

	private double dtbl = 0.00;

	private double crbl = 0.00;

	private double dcam = 0.00;

	private double ccam = 0.00;

	private double dtra = 0.00;

	private double ctra = 0.00;

	private int dcbi = 0;

	private int ccbi = 0;

	private int dtbi = 0;

	private int ctbi = 0;

	private int copa = 0;

	private int ccla = 0;

	private int topa = 0;

	private int tcla = 0;

	private int ream = 0;

	//D004
	private double inam = 0.00;

	private float inrt = 0;

	private String prmk = "";

	//D005
	private String flag = "";

	//D006
	private double inac = 0.00;

	private String jfid = "";

	private String jfdt = "";

	private String jfus = "";

	//D007
	private String fg26 = "";

	private String fg23a = "";

	private String cubr = "";

	//D011
	private String intp = "";

	private String idtp1 = "";
	private String idtp2 = "";
	private String idtp3 = "";
	private String idtp4 = "";

	private String clnm1 = "";

	//D015
	private String usst = "";

	//D016

	private String fg31 = "";

	private String ebno1 = "";
	private String ebno2 = "";
	private String ebno3 = "";
	private String ebno4 = "";

	//D018
	private String bitmap = "";

	private String bag = "";

	//D020
	private int gtcd = 0;

	private String gtdt = "";

	private String gtst = "";

	private String gts1 = "";

	private String tsfl = "";

	private String wknt = "";

	private String usid = "";

	private String optp = "";

	private double apam = 0.00;

	private String brus = "";

	private String bru1 = "";

	private int agtm = 0;

	private int gttm = 0;

	private String fg01 = "";

	private String fg09 = "";
	private String fg29 = "";

	private String amt1 = "";

	private String desc = "";

	//D021
	private int qnum = 0;

	private double qamt = 0.00;

	private int cnum = 0;

	private double camt = 0.00;

	//Pfmt
	private int fldseq = 0;
	private int fldfmt = 0;

	private String flddes = "";

	private String fldnam = "";

	//Ptab
	private String tabtyp = "";

	private String tablis = "";

	private String tabdes = "";

	private String cunm = "";
	private String cunm1= "";

	private String idtpa = "";

	private String idnoa = "";

	private String custa = "";

	private String cunma = "";
	private String cunma1 = "";

	//P001
	private String name = "";

	private String grop = "";

	private String pgm = "";


	private String acod = "";

	private String cyck = "";

	private String mttp = "";

	private String dbag = "";

	private String rem = "";

	//P002
	private String pocudt = "";

	private String poltdt = "";

	private String ponxdt = "";

	private String posts = "";

	private String podofw = "";

	private String powfef = "";

	private String pow25f = "";

	private String powwef = "";

	private String powtef = "";

	private String powmef = "";

	private String powqef = "";

	private String powhef = "";

	private String powyef = "";

	private String powybf = "";

	private int thtm = 0;

	private int lstm = 0;

	//P003
	private String atnm = "";

	private String itat = "";

	private int itlv = 0;

	private String sust = "";

	private String rpst = "";

	private String txtp = "";

	private String inacit = "";

	private String inacit1 = "";

	private String inacit2 = "";

	private String acgp = "";

	private String redf = "";

	private String ictp = "";

	private String smst = "";

	private String tbcl = "";

	private String ocst = "";

	private String ckfg1 = "";

	private int cdnum1 = 0;

	private int cdnum2 = 0;

	private int cdnum3 = 0;

	private String oafg = "";

	//P004
	private String cycd = "";

	private String rgcd = "";

	private int exut = 0;

	private double cuby = 0.00;

	private double cusl = 0.00;

	private double meby = 0.00;

	private double mesl = 0.00;

	private double mdpr = 0.00;

	private double enpr = 0.00;

	private int sbut = 0;

	private String eddt = "";

	private String stcd = "";

	//P005
	private String svcd = "";
	private String svcd1= "";
	private String svcd2= "";

	private int seq = 0;

	private String ioff = "";

	private String ctff = "";

	private String accf = "";

	private String amcf = "";

	private String tscf = "";

	private String cynf = "";

	private String tsaf = "";

	private String cdtf = "";

	private String cdnf = "";

	private String oitf = "";

	private String ptcf = "";

	private String lbcf = "";

	private String vcsf = "";

	private String spec = "";

	private String cuss = "";

	private String accs = "";

	private String cdts = "";

	//P006
	private double mxam = 0.00;

	private double opam = 0.00;

	private double rmam = 0.00;

	private double stam = 0.00;

	private double ttam = 0.00;

	private int crct = 0;

	private int drct = 0;

	//P007
	private String usnm = "";

	private String ustp = "";

	private String usbr = "";

	private String usgp = "";

	private String cvcd = "";

	private String uspw = "";

	private String usdt = "";

	private String tmid = "";
	private String tmid1= "";
	private String tmid2= "";
	private String tmid3= "";
	private String zmno = "";

	private String bast = "";

	private String fg14 = "";

	private String pgdr = "";

	private String tsusa = "";

	private String opbra = "";

	private String otbr = "";

	private String opdtac = "";

	private String frdtac = "";

	private String frtpa = "";

	private String bbr1a = "";

	private String pocda = "";

	private String tsb2 = "";

	private String numac1 = "";

	private String srnoa = "";

	private String fg20a = "";
	private String fg21a = "";
	private String fg22a = "";

	private String cuno1 = "";
	private String cuno2 = "";
	private String cuno3 = "";
	private String cuno4 = "";
	private String cuno5 = "";
	private String cuno6 = "";
	private String cuno7 = "";
	private String cuno8 = "";
	private String cuno9 = "";
	private String cuno10= "";
	private String cuno11= "";
	private String cuno12= "";

	private String acsta = "";

	private String acctac = "";

	private String tscda = "";

	private String sucda = "";

	private String tssta = "";

	private String numac2 = "";

	private String dramac = "";

	private String cramac = "";

	private String drblac = "";
	private String crblac = "";

	private String name1 = "";

	private String lobl = "";

	private float xdra = 0;
	private float rat1 = 0;
	private float rat2 = 0;
	private float rat3 = 0;
	private float rat4 = 0;
	private float rat11= 0;
	private float rat21= 0;
	private float rat31= 0;
	private float rat41= 0;
	private float rat51= 0;

	private double fram = 0.00;

	private String name2 = "";
	private String name3 = "";
	private String name4 = "";
	private String name5 = "";

	private double tsam3 = 0.00;
	private double tsam4 = 0.00;

	private String acit1 = "";
	private String acit2 = "";
	private String acit3 = "";
	private String acit4 = "";
	private String eacd = "";
	private String fg16 = "";
	private String intp1 = "";
	private double dram=0.00;
	private double tsam5=0.00;
	private double tsam6=0.00;
	private double tsam7=0.00;
	private double tsam8=0.00;
	private double tsam9=0.00;
	private double tsam10 = 0.00;
	private double tsam11 = 0.00;
	private double tsam12 = 0.00;

	//P009
	private String brno = "";

	private String brnm = "";

	private String brsn = "";

	private String upbr = "";

	private String upb1 = "";

	private String exlg = "";

	//P010
	private String ebnm = "";

	private String ebst = "";

	private String eblv = "";

	//P011
	private String opt1 = "";

	private String bkno = "";

	private String acct3 = "";
	private String acct4 = "";
	private String acct5 = "";
	private String acct6 = "";
	private String acct7 = "";
	private String acct8 = "";
	private String acct9 = "";
	private String acct10= "";
	private String acct11= "";
	private String acct12= "";


	//P012
	private double val = 0.00;

	private double f01 = 0.00;

	//P013
	private String kywd = "";

	//P015
	private String cist = "";

	private String vabr = "";

	//P016
	private String infr = "";

	private String scdt = "";

	//P017
	private String inpm = "";

	//P019
	private String pmtp = "";

	private String pmky = "";

	private String pmcd = "";

	private String pmnm = "";

	private String pmv1 = "";

	private String pmv2 = "";

	private String pmv3 = "";

	private String pmv4 = "";

	private String pmv5 = "";

	//V007
	private String fg12 = "";

	//V008
	private String rptp = "";

	private int coun = 0;

	private String ouus = "";

	//P021
	private String typ = "";

	private String dpt = "";

	private int inc = 0;

	private int ini = 0;

	private int max = 0;

	//P027
	private String autp = "";

	private double quta = 0.00;

	private double qutt = 0.00;

	private String aufg = "";

	private String stdt = "";

	private String plac = "";

	//P028
	private String bktp = "";

	private String bksn = "";

	private String bknm = "";

	private String bkad = "";

	private String bktl = "";

	private String bkfx = "";

	//P030
	private String von = "";

	private String vons = "";

	private int sttm = 0;

	private int dutm = 0;

	private String sts = "";
	//C003
	private	String	dbnm="";
	//other
	private String cucd = "";

	private String cuma1 = "";

	private String oftl = "";

	private String fg17 = "";

	private String otbk = "";
	private String numac3 = "";
	private String acsf = "";
	private String acsf1= "";
	private String acsf2= "";
	private String acsfa = "";
	private String fesd = "";
	private String feed = "";
	private String fbuf = "";

	//Testjava
	private	String	tablename="";
	private	String	zdname="";
	private	String	zddes="";
	private	String	zdremark="";

	private	byte[]	pin =new byte[8];
	private	byte[]	pin1=new byte[8];
	private	byte[]	pin2=new byte[8];
	private	byte[]	pin3=new byte[8];
	private	byte[]	pin4=new byte[8];
	private	byte[]	newpin=new byte[8];

	//C001
	public String getIdtp() {
		return this.idtp.trim();
	}

	public void setIdtp(String c) {
		this.idtp = TransEncoding(c);
	}

	public String getIdno() {
		return this.idno.trim();
	}

	public void setIdno(String c) {
		this.idno = TransEncoding(c);
	}

	public String getCuno() {
		return TransEncoding(this.cuno);
	}

	public void setCuno(String c) {
		this.cuno = TransEncoding(c);
	}

	public String getCust() {
		return this.cust.trim();
	}

	public void setCust(String c) {
		this.cust = TransEncoding(c);
	}

	public String getCust1() {
		return this.cust1.trim();
	}

	public void setCust1(String c) {
		this.cust1= TransEncoding(c);
	}

	public String getCust2() {
		return this.cust2.trim();
	}

	public void setCust2(String c) {
		this.cust2= TransEncoding(c);
	}
	public String getTsbr() {
		return this.tsbr.trim();
	}

	public void setTsbr(String c) {
		this.tsbr = TransEncoding(c);
	}

	public String getClnm() {
		return this.clnm.trim();
	}

	public void setClnm(String c) {
		this.clnm = TransEncoding(c);
	}

	public String getClnma() {
		return this.clnma.trim();
	}

	public void setClnma(String c) {
		this.clnma = TransEncoding(c);
	}

	public String getClnma1() {
		return this.clnma1.trim();
	}

	public void setClnma1(String c) {
		this.clnma1 = TransEncoding(c);
	}

	public String getAddr() {
		return this.addr.trim();
	}

	public void setAddr(String c) {
		this.addr = TransEncoding(c);
	}

	public String getHadd() {
		return this.hadd.trim();
	}

	public void setHadd(String c) {
		this.hadd = TransEncoding(c);
	}

	public String getRmdf() {
		return this.rmdf.trim();
	}

	public void setRmdf(String c) {
		this.rmdf = TransEncoding(c);
	}

	public double getMnsi() {
		return this.mnsi;
	}

	public void setMnsi(double c) {
		this.mnsi = c;
	}

	public double getMnoi() {
		return this.mnoi;
	}

	public void setMnoi(double c) {
		this.mnoi = c;
	}

	public double getMnti() {
		return this.mnti;
	}

	public void setMnti(double c) {
		this.mnti = c;
	}

	public String getJbst() {
		return this.jbst.trim();
	}

	public void setJbst(String c) {
		this.jbst = TransEncoding(c);
	}

	public String getSxcd() {
		return this.sxcd.trim();
	}

	public void setSxcd(String c) {
		this.sxcd = TransEncoding(c);
	}

	public String getNatl() {
		return this.natl.trim();
	}

	public void setNatl(String c) {
		this.natl = TransEncoding(c);
	}

	public String getMrsu() {
		return this.mrsu.trim();
	}

	public void setMrsu(String c) {
		this.mrsu = TransEncoding(c);
	}

	public String getBtdt() {
		return this.btdt.trim();
	}

	public void setBtdt(String c) {
		this.btdt = TransEncoding(c);
	}
	public void setBtdt(int c) {
		this.btdt = String.valueOf(c);
	}
	public String getAgnm() {
		return this.agnm.trim();
	}

	public void setAgnm(String c) {
		this.agnm = TransEncoding(c);
	}

	public String getAfnm() {
		return this.afnm.trim();
	}

	public void setAfnm(String c) {
		this.afnm = TransEncoding(c);
	}

	public String getAlnm() {
		return this.alnm.trim();
	}

	public void setAlnm(String c) {
		this.alnm = TransEncoding(c);
	}

	public String getCutl() {
		return this.cutl.trim();
	}

	public void setCutl(String c) {
		this.cutl = TransEncoding(c);
	}

	public String getSmmd() {
		return this.smmd.trim();
	}

	public void setSmmd(String c) {
		this.smmd = TransEncoding(c);
	}

	public String getTelv() {
		return this.telv.trim();
	}

	public void setTelv(String c) {
		this.telv = TransEncoding(c);
	}

	public String getEulv() {
		return this.eulv.trim();
	}

	public void setEulv(String c) {
		this.eulv = TransEncoding(c);
	}

	public int getFdcf() {
		return this.fdcf;
	}

	public void setFdcf(int c) {
		this.fdcf = c;
	}

	public String getFdlv() {
		return this.fdlv.trim();
	}

	public void setFdlv(String c) {
		this.fdlv = TransEncoding(c);
	}

	public String getCuma() {
		return this.cuma.trim();
	}

	public void setCuma(String c) {
		this.cuma = TransEncoding(c);
	}

	public String getCuma1() {
		return this.cuma1.trim();
	}

	public void setCuma1(String c) {
		this.cuma1 = TransEncoding(c);
	}

	public String getUscd() {
		return this.uscd.trim();
	}

	public void setUscd(String c) {
		this.uscd = TransEncoding(c);
	}

	public String getEmad() {
		return this.emad.trim();
	}

	public void setEmad(String c) {
		this.emad = TransEncoding(c);
	}

	public String getTele() {
		return this.tele.trim();
	}

	public void setTele(String c) {
		this.tele = TransEncoding(c);
	}

	public String getFxno() {
		return this.fxno.trim();
	}

	public void setFxno(String c) {
		this.fxno = TransEncoding(c);
	}

	public String getPscd() {
		return this.pscd.trim();
	}

	public void setPscd(String c) {
		this.pscd = TransEncoding(c);
	}

	public String getHdag() {
		return this.hdag.trim();
	}

	public void setHdag(String c) {
		this.hdag = TransEncoding(c);
	}

	public String getRgpl() {
		return this.rgpl.trim();
	}

	public void setRgpl(String c) {
		this.rgpl = TransEncoding(c);
	}

	public int getRgpd() {
		return this.rgpd;
	}

	public void setRgpd(int c) {
		this.rgpd = c;
	}

	public String getRgno() {
		return this.rgno.trim();
	}

	public void setRgno(String c) {
		this.rgno = TransEncoding(c);
	}

	public double getRgfn() {
		return this.rgfn;
	}

	public void setRgfn(double c) {
		this.rgfn = c;
	}

	public String getRgdt() {
		return this.rgdt.trim();
	}

	public void setRgdt(String c) {
		this.rgdt = TransEncoding(c);
	}
	public void setRgdt(int c) {
		this.rgdt = String.valueOf(c);
	}
	public String getCmno() {
		return this.cmno.trim();
	}

	public void setCmno(String c) {
		this.cmno = TransEncoding(c);
	}

	public String getBlgu() {
		return this.blgu.trim();
	}

	public void setBlgu(String c) {
		this.blgu = TransEncoding(c);
	}

	public String getFpnm() {
		return this.fpnm.trim();
	}

	public void setFpnm(String c) {
		this.fpnm = TransEncoding(c);
	}

	public String getFpno() {
		return this.fpno.trim();
	}

	public void setFpno(String c) {
		this.fpno = TransEncoding(c);
	}

	public String getFptl() {
		return this.fptl.trim();
	}

	public void setFptl(String c) {
		this.fptl = TransEncoding(c);
	}

	public String getMonm() {
		return this.monm.trim();
	}

	public void setMonm(String c) {
		this.monm = TransEncoding(c);
	}

	public String getMoid() {
		return this.moid.trim();
	}

	public void setMoid(String c) {
		this.moid = TransEncoding(c);
	}

	public String getMotl() {
		return this.motl.trim();
	}

	public void setMotl(String c) {
		this.motl = TransEncoding(c);
	}

	public String getOpno() {
		return this.opno.trim();
	}

	public void setOpno(String c) {
		this.opno = TransEncoding(c);
	}

	public String getLnca() {
		return this.lnca.trim();
	}

	public void setLnca(String c) {
		this.lnca = TransEncoding(c);
	}

	public double getSpam() {
		return this.spam;
	}

	public void setSpam(double c) {
		this.spam = c;
	}

	public String getTxcd() {
		return this.txcd.trim();
	}

	public void setTxcd(String c) {
		this.txcd = TransEncoding(c);
	}

	public String getTxcd1() {
		return this.txcd1.trim();
	}

	public void setTxcd1(String c) {
		this.txcd1 = TransEncoding(c);
	}

	public String getOpdt() {
		return this.opdt.trim();
	}

	public void setOpdt(String c) {
		this.opdt = TransEncoding(c);
	}
	public void setOpdt(int c) {
		this.opdt = String.valueOf(c);
	}
	public String getOpus() {
		return this.opus.trim();
	}

	public void setOpus(String c) {
		this.opus = TransEncoding(c);
	}

	public String getCldt() {
		return this.cldt.trim();
	}

	public void setCldt(String c) {
		this.cldt = TransEncoding(c);
	}
	public void setCldt(int c) {
		this.cldt = String.valueOf(c);
	}
	public String getClus() {
		return this.clus.trim();
	}

	public void setClus(String c) {
		this.clus = TransEncoding(c);
	}

	//V001
	public String getCdtp() {
		return this.cdtp.trim();
	}

	public void setCdtp(String c) {
		this.cdtp = TransEncoding(c);
	}

	public String getCdtp1() {
		return this.cdtp1.trim();
	}

	public void setCdtp1(String c) {
		this.cdtp1 = TransEncoding(c);
	}

	public String getCdtp2() {
		return this.cdtp2.trim();
	}

	public void setCdtp2(String c) {
		this.cdtp2 = TransEncoding(c);
	}

	public String getCdtp3() {
		return this.cdtp3.trim();
	}

	public void setCdtp3(String c) {
		this.cdtp3 = TransEncoding(c);
	}

	public String getCdtp4() {
		return this.cdtp4.trim();
	}

	public void setCdtp4(String c) {
		this.cdtp4 = TransEncoding(c);
	}

	public String getCdtpa() {
		return this.cdtpa.trim();
	}

	public void setCdtpa(String c) {
		this.cdtpa = TransEncoding(c);
	}

	public String getCdno() {
		return this.cdno.trim();
	}

	public void setCdno(String c) {
		this.cdno = TransEncoding(c);
	}

	public String getCdnoa() {
		return this.cdnoa.trim();
	}

	public void setCdnoa(String c) {
		this.cdnoa = TransEncoding(c);
	}

	public String getEbno() {
		return this.ebno.trim();
	}

	public void setEbno(String c) {
		this.ebno = TransEncoding(c);
	}

	public String getEbnoa() {
		return this.ebnoa.trim();
	}

	public void setEbnoa(String c) {
		this.ebnoa = TransEncoding(c);
	}

	public String getCunoa() {
		return this.cunoa.trim();
	}

	public void setCunoa(String c) {
		this.cunoa = TransEncoding(c);
	}

	public String getCunm() {
		return this.cunm.trim();
	}

	public void setCunm(String c) {
		this.cunm = TransEncoding(c);
	}

	public String getCunm1() {
		return this.cunm1.trim();
	}

	public void setCunm1(String c) {
		this.cunm1= TransEncoding(c);
	}

	public String getInbr() {
		return this.inbr.trim();
	}

	public void setInbr(String c) {
		this.inbr = TransEncoding(c);
	}

	public String getIndt() {
		return this.indt;
	}

	public void setIndt(String c) {
		this.indt = c;
	}
	public void setIndt(int c) {
		this.indt = String.valueOf(c);
	}
	public String getInus() {
		return this.inus.trim();
	}

	public void setInus(String c) {
		this.inus = TransEncoding(c);
	}

	public String getEfdt() {
		return this.efdt;
	}

	public void setEfdt(String c) {
		this.efdt = TransEncoding(c);
	}
	public void setEfdt(int c) {
		this.efdt = String.valueOf(c);
	}
	public String getVlpd() {
		return this.vlpd.trim();
	}

	public void setVlpd(String c) {
		this.vlpd = TransEncoding(c);
	}

	public String getIvdt() {
		return this.ivdt;
	}

	public void setIvdt(String c) {
		this.ivdt = c;
	}
	public void setIvdt(int c) {
		this.ivdt = String.valueOf(c);
	}
	public String getCvc1() {
		return this.cvc1.trim();
	}

	public void setCvc1(String c) {
		this.cvc1 = TransEncoding(c);
	}

	public String getCvc2() {
		return this.cvc2.trim();
	}

	public void setCvc2(String c) {
		this.cvc2 = TransEncoding(c);
	}

	public String getSucd() {
		return this.sucd.trim();
	}

	public void setSucd(String c) {
		this.sucd = TransEncoding(c);
	}

	public String getAtpw() {
		return this.atpw.trim();
	}

	public void setAtpw(String c) {
		this.atpw = TransEncoding(c);
	}

	public String getAtpw1() {
		return this.atpw1.trim();
	}

	public void setAtpw1(String c) {
		this.atpw1 = TransEncoding(c);
	}

	public String getAtpw2() {
		return this.atpw2.trim();
	}

	public void setAtpw2(String c) {
		this.atpw2 = TransEncoding(c);
	}

	public String getTspw() {
		return this.tspw.trim();
	}

	public void setTspw(String c) {
		this.tspw = TransEncoding(c);
	}

	public String getTspw1() {
		return this.tspw1.trim();
	}

	public void setTspw1(String c) {
		this.tspw1= TransEncoding(c);
	}

	public String getTspw2() {
		return this.tspw2.trim();
	}

	public void setTspw2(String c) {
		this.tspw2= TransEncoding(c);
	}

	public int getPera() {
		return this.pera;
	}

	public void setPera(int c) {
		this.pera = c;
	}

	public int getUpra() {
		return this.upra;
	}

	public void setUpra(int c) {
		this.upra = c;
	}

	public int getSmrf() {
		return this.smrf;
	}

	public void setSmrf(int c) {
		this.smrf = c;
	}

	public int getSmpg() {
		return this.smpg;
	}

	public void setSmpg(int c) {
		this.smpg = c;
	}

	public int getSmln() {
		return this.smln;
	}

	public void setSmln(int c) {
		this.smln = c;
	}

	public String getLtdt() {
		return this.ltdt;
	}

	public void setLtdt(String c) {
		this.ltdt = c;
	}
	public void setLtdt(int c) {
		this.ltdt = String.valueOf(c);
	}

	public int getLtrf() {
		return this.ltrf;
	}

	public void setLtrf(int c) {
		this.ltrf = c;
	}

	public String getPtcd() {
		return this.ptcd.trim();
	}

	public void setPtcd(String c) {
		this.ptcd = TransEncoding(c);
	}

	public String getRldt() {
		return this.rldt;
	}

	public void setRldt(String c) {
		this.rldt = c;
	}
	public void setRldt(int c) {
		this.rldt = String.valueOf(c);
	}

	public String getRlus() {
		return this.rlus.trim();
	}

	public void setRlus(String c) {
		this.rlus = TransEncoding(c);
	}

	public String getRlcd() {
		return this.rlcd.trim();
	}

	public void setRlcd(String c) {
		this.rlcd = TransEncoding(c);
	}

	public String getCdst() {
		return this.cdst.trim();
	}

	public void setCdst(String c) {
		this.cdst = TransEncoding(c);
	}

	public String getCdst1() {
		return this.cdst1.trim();
	}

	public void setCdst1(String c) {
		this.cdst1 = TransEncoding(c);
	}

	public String getCdsta() {
		return this.cdsta.trim();
	}

	public void setCdsta(String c) {
		this.cdsta = TransEncoding(c);
	}

	public String getFfdt() {
		return this.ffdt;
	}

	public void setFfdt(String c) {
		this.ffdt = c;
	}
	public void setFfdt(int c) {
		this.ffdt = String.valueOf(c);
	}

	public String getFfus() {
		return this.ffus.trim();
	}

	public void setFfus(String c) {
		this.ffus = TransEncoding(c);
	}

	public String getSpct() {
		return this.spct.trim();
	}

	public void setSpct(String c) {
		this.spct = TransEncoding(c);
	}

	// V002
	public String getAcct() {
		return this.acct.trim();
	}

	public void setAcct(String c) {
		this.acct = TransEncoding(c);
	}

	public String getAccta() {
		return this.accta.trim();
	}

	public void setAccta(String c) {
		this.accta = TransEncoding(c);
	}

	//D001
	public String getTsdt() {
		return this.tsdt.trim();
	}

	public void setTsdt(String c) {
		this.tsdt = TransEncoding(c);
	}

	public void setTsdt(int c) {
		//this.tsdt = ""+c;
		this.tsdt = String.valueOf(c);
		//this.tsdt = Integer.toString(c);
	}

	public int getTsrf() {
		return this.tsrf;
	}

	public void setTsrf(int c) {
		this.tsrf = c;
	}

	public int getTsrf1() {
		return this.tsrf1;
	}

	public void setTsrf1(int c) {
		this.tsrf1 = c;
	}

	public int getTsrf2() {
		return this.tsrf2;
	}

	public void setTsrf2(int c) {
		this.tsrf2 = c;
	}

	public int getTsrf3() {
		return this.tsrf3;
	}

	public void setTsrf3(int c) {
		this.tsrf3 = c;
	}

	public int getTsrf4() {
		return this.tsrf4;
	}

	public void setTsrf4(int c) {
		this.tsrf4 = c;
	}

	public int getTsrf5() {
		return this.tsrf5;
	}

	public void setTsrf5(int c) {
		this.tsrf5 = c;
	}

	public int getTsrf6() {
		return this.tsrf6;
	}

	public void setTsrf6(int c) {
		this.tsrf6 = c;
	}

	public int getTsrf7() {
		return this.tsrf7;
	}

	public void setTsrf7(int c) {
		this.tsrf7 = c;
	}

	public int getTsrf8() {
		return this.tsrf8;
	}

	public void setTsrf8(int c) {
		this.tsrf8 = c;
	}

	public int getPgno() {
		return this.pgno;
	}

	public void setPgno(int c) {
		this.pgno = c;
	}

	public int getTstm() {
		return this.tstm;
	}

	public void setTstm(int c) {
		this.tstm = c;
	}

	public int getTstm1() {
		return this.tstm1;
	}

	public void setTstm1(int c) {
		this.tstm1 = c;
	}

	public int getTstm2() {
		return this.tstm2;
	}

	public void setTstm2(int c) {
		this.tstm2 = c;
	}

	public int getTstm3() {
		return this.tstm3;
	}

	public void setTstm3(int c) {
		this.tstm3 = c;
	}

	public int getTstm4() {
		return this.tstm4;
	}

	public void setTstm4(int c) {
		this.tstm4 = c;
	}

	public int getTstm5() {
		return this.tstm5;
	}

	public void setTstm5(int c) {
		this.tstm5 = c;
	}

	public int getTstm6() {
		return this.tstm6;
	}

	public void setTstm6(int c) {
		this.tstm6 = c;
	}

	public int getTstm7() {
		return this.tstm7;
	}

	public void setTstm7(int c) {
		this.tstm7 = c;
	}

	public int getTstm8() {
		return this.tstm8;
	}

	public void setTstm8(int c) {
		this.tstm8 = c;
	}

	public int getTstm9() {
		return this.tstm9;
	}

	public void setTstm9(int c) {
		this.tstm9 = c;
	}

	public String getWrtp() {
		return this.wrtp.trim();
	}

	public void setWrtp(String c) {
		this.wrtp = TransEncoding(c);
	}

	public String getWrtpa() {
		return this.wrtpa.trim();
	}

	public void setWrtpa(String c) {
		this.wrtpa= TransEncoding(c);
	}

	public String getPrid() {
		return this.prid.trim();
	}

	public void setPrid(String c) {
		this.prid = TransEncoding(c);
	}

	public String getPrid1() {
		return this.prid1.trim();
	}

	public void setPrid1(String c) {
		this.prid1 = TransEncoding(c);
	}

	public String getBstp() {
		return this.bstp.trim();
	}

	public void setBstp(String c) {
		this.bstp = TransEncoding(c);
	}

	public String getBstpa() {
		return this.bstpa.trim();
	}

	public void setBstpa(String c) {
		this.bstpa= TransEncoding(c);
	}

	public String getFg11() {
		return this.fg11.trim();
	}

	public void setFg11(String c) {
		this.fg11 = TransEncoding(c);
	}

	public String getAcct1() {
		return this.acct1.trim();
	}

	public void setAcct1(String c) {
		this.acct1 = TransEncoding(c);
	}

	public String getAcct2() {
		return this.acct2.trim();
	}

	public void setAcct2(String c) {
		this.acct2 = TransEncoding(c);
	}

	public String getAcit() {
		return this.acit.trim();
	}

	public void setAcit(String c) {
		this.acit = TransEncoding(c);
	}

	public String getAcita() {
		return this.acita.trim();
	}

	public void setAcita(String c) {
		this.acita = TransEncoding(c);
	}

	public String getCyno() {
		return this.cyno.trim();
	}

	public void setCyno(String c) {
		this.cyno = TransEncoding(c);
	}

	public String getCyno1() {
		return this.cyno1.trim();
	}

	public void setCyno1(String c) {
		this.cyno1 = TransEncoding(c);
	}

	public String getCyno2() {
		return this.cyno2.trim();
	}

	public void setCyno2(String c) {
		this.cyno2 = TransEncoding(c);
	}

	public String getCyno3() {
		return this.cyno3.trim();
	}

	public void setCyno3(String c) {
		this.cyno3 = TransEncoding(c);
	}

	public String getCyno4() {
		return this.cyno4.trim();
	}

	public void setCyno4(String c) {
		this.cyno4 = TransEncoding(c);
	}

	public String getCyno5() {
		return this.cyno5.trim();
	}

	public void setCyno5(String c) {
		this.cyno5 = TransEncoding(c);
	}

	public String getCyno6() {
		return this.cyno6.trim();
	}

	public void setCyno6(String c) {
		this.cyno6 = TransEncoding(c);
	}

	public String getCyno7() {
		return this.cyno7.trim();
	}

	public void setCyno7(String c) {
		this.cyno7 = TransEncoding(c);
	}

	public String getCyno8() {
		return this.cyno8.trim();
	}

	public void setCyno8(String c) {
		this.cyno8 = TransEncoding(c);
	}

	public String getCyno9() {
		return this.cyno9.trim();
	}

	public void setCyno9(String c) {
		this.cyno9 = TransEncoding(c);
	}

	public String getCyno10() {
		return this.cyno10.trim();
	}

	public void setCyno10(String c) {
		this.cyno10 = TransEncoding(c);
	}

	public String getCyno11() {
		return this.cyno11.trim();
	}

	public void setCyno11(String c) {
		this.cyno11 = TransEncoding(c);
	}

	public String getCyno12() {
		return this.cyno12.trim();
	}

	public void setCyno12(String c) {
		this.cyno12 = TransEncoding(c);
	}

	public String getCynoa() {
		return this.cynoa.trim();
	}

	public void setCynoa(String c) {
		this.cynoa = TransEncoding(c);
	}

	public String getCyfl() {
		return this.cyfl.trim();
	}

	public void setCyfl(String c) {
		this.cyfl = TransEncoding(c);
	}

	public double getTsam() {
		return this.tsam;
	}

	public void setTsam(double c) {
		this.tsam = c;
	}

	public double getTsam3() {
		return this.tsam3;
	}

	public void setTsam3(double c) {
		this.tsam3 = c;
	}

	public double getTsam4() {
		return this.tsam4;
	}

	public void setTsam4(double c) {
		this.tsam4 = c;
	}

	public String getTsamac() {
		return this.tsamac;
	}

	public void setTsamac(String c) {
		this.tsamac = TransEncoding(c);
	}

	public String getTsamac1() {
		return this.tsamac1;
	}

	public void setTsamac1(String c) {
		this.tsamac1= TransEncoding(c);
	}

	public String getTsamac2() {
		return this.tsamac2;
	}

	public void setTsamac2(String c) {
		this.tsamac2= TransEncoding(c);
	}

	public double getTsam1() {
		return this.tsam1;
	}

	public void setTsam1(double c) {
		this.tsam1 = c;
	}

	public double getTsam2() {
		return this.tsam2;
	}

	public void setTsam2(double c) {
		this.tsam2 = c;
	}

	public double getGdbl() {
		return this.gdbl;
	}

	public void setGdbl(double c) {
		this.gdbl = c;
	}

	public double getGcbl() {
		return this.gcbl;
	}

	public void setGcbl(double c) {
		this.gcbl = c;
	}

	public double getAdbl() {
		return this.adbl;
	}

	public void setAdbl(double c) {
		this.adbl = c;
	}

	public int getNum() {
		return this.num;
	}

	public void setNum(int c) {
		this.num = c;
	}

	public String getNumac() {
		return this.numac;
	}

	public void setNumac(String c) {
		this.numac = TransEncoding(c);
	}

	public String getStno() {
		return this.stno.trim();
	}

	public void setStno(String c) {
		this.stno = TransEncoding(c);
	}

	public String getStnoa() {
		return this.stnoa.trim();
	}

	public void setStnoa(String c) {
		this.stnoa = TransEncoding(c);
	}

	public String getEdno() {
		return this.edno.trim();
	}

	public void setEdno(String c) {
		this.edno = TransEncoding(c);
	}

	public String getEdnoa() {
		return this.ednoa.trim();
	}

	public void setEdnoa(String c) {
		this.ednoa = TransEncoding(c);
	}

	public String getTscd() {
		return this.tscd.trim();
	}

	public void setTscd(String c) {
		this.tscd = TransEncoding(c);
	}

	public String getTsus() {
		return this.tsus.trim();
	}

	public void setTsus(String c) {
		this.tsus = TransEncoding(c);
	}

	public String getCkus() {
		return this.ckus.trim();
	}

	public void setCkus(String c) {
		this.ckus = TransEncoding(c);
	}

	public String getAuus() {
		return this.auus.trim();
	}

	public void setAuus(String c) {
		this.auus = TransEncoding(c);
	}

	public String getTsst() {
		return this.tsst.trim();
	}

	public void setTsst(String c) {
		this.tsst = TransEncoding(c);
	}

	public String getRvdt() {
		return this.rvdt.trim();
	}

	public void setRvdt(String c) {
		this.rvdt = TransEncoding(c);
	}
	public void setRvdt(int c) {
		this.rvdt = String.valueOf(c);
	}

	public int getRvrf() {
		return this.rvrf;
	}

	public void setRvrf(int c) {
		this.rvrf = c;
	}

	public String getComm() {
		return this.comm.trim();
	}

	public void setComm(String c) {
		this.comm = TransEncoding(c);
	}

	//P014
	public String getErcd() {
		return this.ercd.trim();
	}

	public void setErcd(String c) {
		this.ercd = TransEncoding(c);
	}

	public String getErmg() {
		return this.ermg.trim();
	}

	public void setErmg(String c) {
		this.ermg = TransEncoding(c);
	}

	public String getApmg() {
		return this.apmg.trim();
	}

	public void setApmg(String c) {
		this.apmg = TransEncoding(c);
	}

	//D002
	public int getVcrf() {
		return this.vcrf;
	}

	public void setVcrf(int c) {
		this.vcrf = c;
	}

	public int getVcrf1() {
		return this.vcrf1;
	}

	public void setVcrf1(int c) {
		this.vcrf1 = c;
	}

	public String getAcbr() {
		return this.acbr.trim();
	}

	public void setAcbr(String c) {
		this.acbr = TransEncoding(c);
	}

	public String getCkbr() {
		return this.ckbr.trim();
	}

	public void setCkbr(String c) {
		this.ckbr = TransEncoding(c);
	}

	public String getTsfm() {
		return this.tsfm.trim();
	}

	public void setTsfm(String c) {
		this.tsfm = TransEncoding(c);
	}

	public String getOtcd() {
		return this.otcd.trim();
	}

	public void setOtcd(String c) {
		this.otcd = TransEncoding(c);
	}

	public String getOtcd1() {
		return this.otcd1.trim();
	}

	public void setOtcd1(String c) {
		this.otcd1= TransEncoding(c);
	}

	public String getOtcd2() {
		return this.otcd2.trim();
	}

	public void setOtcd2(String c) {
		this.otcd2= TransEncoding(c);
	}

	public String getAmcd() {
		return this.amcd.trim();
	}

	public void setAmcd(String c) {
		this.amcd = TransEncoding(c);
	}

	public String getAmcd1() {
		return this.amcd1.trim();
	}

	public void setAmcd1(String c) {
		this.amcd1= TransEncoding(c);
	}

	public String getAmcda() {
		return this.amcda.trim();
	}

	public void setAmcda(String c) {
		this.amcda = TransEncoding(c);
	}

	public String getBldr() {
		return this.bldr.trim();
	}

	public void setBldr(String c) {
		this.bldr = TransEncoding(c);
	}

	public double getAcbl() {
		return this.acbl;
	}

	public void setAcbl(double c) {
		this.acbl = c;
	}

	public double getAcbl1() {
		return this.acbl1;
	}

	public void setAcbl1(double c) {
		this.acbl1 = c;
	}

	public double getAcbl2() {
		return this.acbl2;
	}

	public void setAcbl2(double c) {
		this.acbl2 = c;
	}

	public double getAcbl3() {
		return this.acbl3;
	}

	public void setAcbl3(double c) {
		this.acbl3 = c;
	}
	public double getAcbl4() {
		return this.acbl4;
	}

	public void setAcbl4(double c) {
		this.acbl4 = c;
	}
	public double getAcbl5() {
		return this.acbl5;
	}

	public void setAcbl5(double c) {
		this.acbl5 = c;
	}
	public double getAcbl6() {
		return this.acbl6;
	}

	public void setAcbl6(double c) {
		this.acbl6 = c;
	}
	public double getAcbl7() {
		return this.acbl7;
	}

	public void setAcbl7(double c) {
		this.acbl7 = c;
	}
	public double getAcbl8() {
		return this.acbl8;
	}

	public void setAcbl8(double c) {
		this.acbl8 = c;
	}
	public String getAcblac() {
		return this.acblac;
	}

	public void setAcblac(String c) {
		this.acblac = TransEncoding(c);
	}

	public String getToit() {
		return this.toit.trim();
	}

	public void setToit(String c) {
		this.toit = TransEncoding(c);
	}

	public double getInat() {
		return this.inat;
	}

	public void setInat(double c) {
		this.inat = c;
	}

	public String getTsb1() {
		return this.tsb1.trim();
	}

	public void setTsb1(String c) {
		this.tsb1 = TransEncoding(c);
	}

	public String getVcst() {
		return this.vcst.trim();
	}

	public void setVcst(String c) {
		this.vcst = TransEncoding(c);
	}

	public String getBlno() {
		return this.blno.trim();
	}

	public void setBlno(String c) {
		this.blno = TransEncoding(c);
	}

	public String getBlno1() {
		return this.blno1.trim();
	}

	public void setBlno1(String c) {
		this.blno1= TransEncoding(c);
	}
	public String getBlno2() {
		return this.blno2.trim();
	}

	public void setBlno2(String c) {
		this.blno2= TransEncoding(c);
	}
	public int getSrno() {
		return this.srno;
	}

	public void setSrno(int c) {
		this.srno = c;
	}

	public int getSrno1() {
		return this.srno1;
	}

	public void setSrno1(int c) {
		this.srno1 = c;
	}

	public int getSrno2() {
		return this.srno2;
	}

	public void setSrno2(int c) {
		this.srno2 = c;
	}

	public int getSrno3() {
		return this.srno3;
	}

	public void setSrno3(int c) {
		this.srno3 = c;
	}

	public String getAnno() {
		return this.anno.trim();
	}

	public void setAnno(String c) {
		this.anno = TransEncoding(c);
	}

	public String getUsno() {
		return this.usno.trim();
	}

	public void setUsno(String c) {
		this.usno = TransEncoding(c);
	}

	public String getFlg9() {
		return this.flg9.trim();
	}

	public void setFlg9(String c) {
		this.flg9 = TransEncoding(c);
	}

	public String getIdno1() {
		return this.idno1.trim();
	}

	public void setIdno1(String c) {
		this.idno1 = TransEncoding(c);
	}

	public String getIofg() {
		return this.iofg.trim();
	}

	public void setIofg(String c) {
		this.iofg = TransEncoding(c);
	}

	//D003
	public String getSvnt() {
		return this.svnt.trim();
	}

	public void setSvnt(String c) {
		this.svnt = TransEncoding(c);
	}

	public String getPocd() {
		return this.pocd.trim();
	}

	public void setPocd(String c) {
		this.pocd = TransEncoding(c);
	}

	public String getPocd1() {
		return this.pocd1.trim();
	}

	public void setPocd1(String c) {
		this.pocd1 = TransEncoding(c);
	}
	public String getVldt() {
		return this.vldt.trim();
	}

	public void setVldt(String c) {
		this.vldt = TransEncoding(c);
	}
	public void setVldt(int c) {
		this.vldt = String.valueOf(c);
	}

	public String getMudt() {
		return this.mudt.trim();
	}

	public void setMudt(String c) {
		this.mudt = TransEncoding(c);
	}
	public void setMudt(int c) {
		this.mudt = String.valueOf(c);
	}

	public String getMudt1() {
		return this.mudt1.trim();
	}

	public void setMudt1(String c) {
		this.mudt1= TransEncoding(c);
	}

	public String getMudt2() {
		return this.mudt2.trim();
	}

	public void setMudt2(String c) {
		this.mudt2= TransEncoding(c);
	}


	public String getMudt3() {
		return this.mudt3.trim();
	}

	public void setMudt3(String c) {
		this.mudt3= TransEncoding(c);
	}


	public String getMudt4() {
		return this.mudt4.trim();
	}

	public void setMudt4(String c) {
		this.mudt4= TransEncoding(c);
	}


	public String getMudt5() {
		return this.mudt5.trim();
	}

	public void setMudt5(String c) {
		this.mudt5= TransEncoding(c);
	}


	public String getMudt6() {
		return this.mudt6.trim();
	}

	public void setMudt6(String c) {
		this.mudt6= TransEncoding(c);
	}


	public String getMudt7() {
		return this.mudt7.trim();
	}

	public void setMudt7(String c) {
		this.mudt7= TransEncoding(c);
	}

	public String getMudt8() {
		return this.mudt8.trim();
	}

	public void setMudt8(String c) {
		this.mudt8= TransEncoding(c);
	}

	public String getMudt9() {
		return this.mudt9.trim();
	}

	public void setMudt9(String c) {
		this.mudt9= TransEncoding(c);
	}

	public String getMudt10() {
		return this.mudt10.trim();
	}

	public void setMudt10(String c) {
		this.mudt10= TransEncoding(c);
	}

	public String getMudt11() {
		return this.mudt11.trim();
	}

	public void setMudt11(String c) {
		this.mudt11= TransEncoding(c);
	}

	public String getMudt12() {
		return this.mudt12.trim();
	}

	public void setMudt12(String c) {
		this.mudt12= TransEncoding(c);
	}

	public String getXddt() {
		return this.xddt.trim();
	}

	public void setXddt(String c) {
		this.xddt = TransEncoding(c);
	}
	public void setXddt(int c) {
		this.xddt = String.valueOf(c);
	}

	public String getNxdt() {
		return this.nxdt.trim();
	}

	public void setNxdt(String c) {
		this.nxdt = TransEncoding(c);
	}
	public void setNxdt(int c) {
		this.nxdt = String.valueOf(c);
	}

	public float getInrd() {
		return this.inrd;
	}

	public void setInrd(float c) {
		this.inrd = c;
	}

	public float getInrc() {
		return this.inrc;
	}

	public void setInrc(float c) {
		this.inrc = c;
	}

	public float getInro() {
		return this.inro;
	}

	public void setInro(float c) {
		this.inro = c;
	}

	public double getDrjs() {
		return this.drjs;
	}

	public void setDrjs(double c) {
		this.drjs = c;
	}

	public double getCrjs() {
		return this.crjs;
	}

	public void setCrjs(double c) {
		this.crjs = c;
	}

	public double getOvjg() {
		return this.ovjg;
	}

	public void setOvjg(double c) {
		this.ovjg = c;
	}

	public double getPaac() {
		return this.paac;
	}

	public void setPaac(double c) {
		this.paac = c;
	}

	public int getTmdb() {
		return this.tmdb;
	}

	public void setTmdb(int c) {
		this.tmdb = c;
	}

	public double getAmdb() {
		return this.amdb;
	}

	public void setAmdb(double c) {
		this.amdb = c;
	}

	public int getTmcr() {
		return this.tmcr;
	}

	public void setTmcr(int c) {
		this.tmcr = c;
	}

	public double getAmcr() {
		return this.amcr;
	}

	public void setAmcr(double c) {
		this.amcr = c;
	}

	public double getIcin() {
		return this.icin;
	}

	public void setIcin(double c) {
		this.icin = c;
	}

	public double getPyin() {
		return this.pyin;
	}

	public void setPyin(double c) {
		this.pyin = c;
	}

	public double getAiin() {
		return this.aiin;
	}

	public void setAiin(double c) {
		this.aiin = c;
	}

	public double getApin() {
		return this.apin;
	}

	public void setApin(double c) {
		this.apin = c;
	}

	public double getPdam() {
		return this.pdam;
	}

	public void setPdam(double c) {
		this.pdam = c;
	}

	public double getFeam() {
		return this.feam;
	}

	public void setFeam(double c) {
		this.feam = c;
	}

	public double getRam1() {
		return this.ram1;
	}

	public void setRam1(double c) {
		this.ram1 = c;
	}

	public double getRam2() {
		return this.ram2;
	}

	public void setRam2(double c) {
		this.ram2 = c;
	}

	public double getOdam() {
		return this.odam;
	}

	public void setOdam(double c) {
		this.odam = c;
	}

	public String getDrpr() {
		return this.drpr.trim();
	}

	public void setDrpr(String c) {
		this.drpr = TransEncoding(c);
	}

	public double getBlpr() {
		return this.blpr;
	}

	public void setBlpr(double c) {
		this.blpr = c;
	}

	public String getUpdt() {
		return this.updt.trim();
	}

	public void setUpdt(String c) {
		this.updt = TransEncoding(c);
	}
	public void setUpdt(int c) {
		this.updt = String.valueOf(c);
	}

	public String getAcst() {
		return this.acst.trim();
	}

	public void setAcst(String c) {
		this.acst = TransEncoding(c);
	}

	public String getAcst1() {
		return this.acst1.trim();
	}

	public void setAcst1(String c) {
		this.acst1= TransEncoding(c);
	}

	public String getAcst2() {
		return this.acst2.trim();
	}

	public void setAcst2(String c) {
		this.acst2= TransEncoding(c);
	}
	public String getAcst3() {
		return this.acst3.trim();
	}

	public void setAcst3(String c) {
		this.acst3= TransEncoding(c);
	}
	public String getAcst4() {
		return this.acst4.trim();
	}

	public void setAcst4(String c) {
		this.acst4= TransEncoding(c);
	}
	public double getAmpa() {
		return this.ampa;
	}

	public void setAmpa(double c) {
		this.ampa = c;
	}

	public String getAutr() {
		return this.autr.trim();
	}

	public void setAutr(String c) {
		this.autr = TransEncoding(c);
	}

	public int getAnum() {
		return this.anum;
	}

	public void setAnum(int c) {
		this.anum = c;
	}

	public int getPrnm() {
		return this.prnm;
	}

	public void setPrnm(int c) {
		this.prnm = c;
	}

	public int getDrnu() {
		return this.drnu;
	}

	public void setDrnu(int c) {
		this.drnu = c;
	}

	public int getCrnu() {
		return this.crnu;
	}

	public void setCrnu(int c) {
		this.crnu = c;
	}

	public String getFrus() {
		return this.frus.trim();
	}

	public void setFrus(String c) {
		this.frus = TransEncoding(c);
	}

	public String getFrdt() {
		return this.frdt.trim();
	}

	public void setFrdt(String c) {
		this.frdt = TransEncoding(c);
	}
	public void setFrdt(int c) {
		this.frdt = String.valueOf(c);
	}

	public String getFrcd() {
		return this.frcd.trim();
	}

	public void setFrcd(String c) {
		this.frcd = TransEncoding(c);
	}

	public String getCkfg() {
		return this.ckfg.trim();
	}

	public void setCkfg(String c) {
		this.ckfg = TransEncoding(c);
	}

	public String getFlg2() {
		return this.flg2.trim();
	}

	public void setFlg2(String c) {
		this.flg2 = TransEncoding(c);
	}

	public String getFlg3() {
		return this.flg3.trim();
	}

	public void setFlg3(String c) {
		this.flg3 = TransEncoding(c);
	}

	public String getFlg4() {
		return this.flg4.trim();
	}

	public void setFlg4(String c) {
		this.flg4 = TransEncoding(c);
	}

	public String getFlg5() {
		return this.flg5.trim();
	}

	public void setFlg5(String c) {
		this.flg5 = TransEncoding(c);
	}

	public int getCoym() {
		return this.coym;
	}

	public void setCoym(int c) {
		this.coym = c;
	}

	public int getBnum() {
		return this.bnum;
	}

	public void setBnum(int c) {
		this.bnum = c;
	}

	public String getIiac() {
		return this.iiac;
	}

	public void setIiac(String c) {
		this.iiac = TransEncoding(c);
	}

	//G001
	public String getAcdt() {
		return this.acdt.trim();
	}

	public void setAcdt(String c) {
		this.acdt = TransEncoding(c);
	}
	public void setAcdt(int c) {
		this.acdt = String.valueOf(c);
	}

	public String getTatp() {
		return this.tatp.trim();
	}

	public void setTatp(String c) {
		this.tatp = TransEncoding(c);
	}

	public String getBlat() {
		return this.blat.trim();
	}

	public void setBlat(String c) {
		this.blat = TransEncoding(c);
	}

	public double getDlbl() {
		return this.dlbl;
	}

	public void setDlbl(double c) {
		this.dlbl = c;
	}

	public double getClbl() {
		return this.clbl;
	}

	public void setClbl(double c) {
		this.clbl = c;
	}

	public double getDtam() {
		return this.dtam;
	}

	public void setDtam(double c) {
		this.dtam = c;
	}

	public double getCram() {
		return this.cram;
	}

	public void setCram(double c) {
		this.cram = c;
	}

	public double getDtbl() {
		return this.dtbl;
	}

	public void setDtbl(double c) {
		this.dtbl = c;
	}

	public double getCrbl() {
		return this.crbl;
	}

	public void setCrbl(double c) {
		this.crbl = c;
	}

	public double getDcam() {
		return this.dcam;
	}

	public void setDcam(double c) {
		this.dcam = c;
	}

	public double getCcam() {
		return this.ccam;
	}

	public void setCcam(double c) {
		this.ccam = c;
	}

	public double getDtra() {
		return this.dtra;
	}

	public void setDtra(double c) {
		this.dtra = c;
	}

	public double getCtra() {
		return this.ctra;
	}

	public void setCtra(double c) {
		this.ctra = c;
	}

	public int getDcbi() {
		return this.dcbi;
	}

	public void setDcbi(int c) {
		this.dcbi = c;
	}

	public int getCcbi() {
		return this.ccbi;
	}

	public void setCcbi(int c) {
		this.ccbi = c;
	}

	public int getDtbi() {
		return this.dtbi;
	}

	public void setDtbi(int c) {
		this.dtbi = c;
	}

	public int getCtbi() {
		return this.ctbi;
	}

	public void setCtbi(int c) {
		this.ctbi = c;
	}

	public int getCopa() {
		return this.copa;
	}

	public void setCopa(int c) {
		this.copa = c;
	}

	public int getCcla() {
		return this.ccla;
	}

	public void setCcla(int c) {
		this.ccla = c;
	}

	public int getTopa() {
		return this.topa;
	}

	public void setTopa(int c) {
		this.topa = c;
	}

	public int getTcla() {
		return this.tcla;
	}

	public void setTcla(int c) {
		this.tcla = c;
	}

	public int getReam() {
		return this.ream;
	}

	public void setReam(int c) {
		this.ream = c;
	}

	//D004
	public double getInam() {
		return this.inam;
	}

	public void setInam(double c) {
		this.inam = c;
	}

	public float getInrt() {
		return this.inrt;
	}

	public void setInrt(float c) {
		this.inrt = c;
	}

	public String getPrmk() {
		return this.prmk.trim();
	}

	public void setPrmk(String c) {
		this.prmk = TransEncoding(c);
	}

	//D005
	public String getFlag() {
		return this.flag.trim();
	}

	public void setFlag(String c) {
		this.flag = TransEncoding(c);
	}

	//D006

	public double getInac() {
		return this.inac;
	}

	public void setInac(double c) {
		this.inac = c;
	}

	public String getJfid() {
		return this.jfid.trim();
	}

	public void setJfid(String c) {
		this.jfid = TransEncoding(c);
	}

	public String getJfdt() {
		return this.jfdt.trim();
	}

	public void setJfdt(String c) {
		this.jfdt = TransEncoding(c);
	}
	public void setJfdt(int c) {
		this.jfdt = String.valueOf(c);
	}

	public String getJfus() {
		return this.jfus.trim();
	}

	public void setJfus(String c) {
		this.jfus = TransEncoding(c);
	}

	//D007
	public String getFg26() {
		return this.fg26.trim();
	}

	public void setFg26(String c) {
		this.fg26 = TransEncoding(c);
	}

	public String getFg23a() {
		return this.fg23a.trim();
	}

	public void setFg23a(String c) {
		this.fg23a = TransEncoding(c);
	}

	public String getCubr() {
		return this.cubr.trim();
	}

	public void setCubr(String c) {
		this.cubr = TransEncoding(c);
	}

	//D011

	public String getIntp() {
		return this.intp.trim();
	}

	public void setIntp(String c) {
		this.intp = TransEncoding(c);
	}

	public String getIdtp1() {
		return this.idtp1.trim();
	}

	public void setIdtp1(String c) {
		this.idtp1 = TransEncoding(c);
	}

	public String getIdtp2() {
		return this.idtp2.trim();
	}

	public void setIdtp2(String c) {
		this.idtp2 = TransEncoding(c);
	}

	public String getIdtp3() {
		return this.idtp3.trim();
	}

	public void setIdtp3(String c) {
		this.idtp3 = TransEncoding(c);
	}

	public String getIdtp4() {
		return this.idtp4.trim();
	}

	public void setIdtp4(String c) {
		this.idtp4 = TransEncoding(c);
	}

	public String getClnm1() {
		return this.clnm1.trim();
	}

	public void setClnm1(String c) {
		this.clnm1 = TransEncoding(c);
	}

	public String getUsst() {
		return this.usst.trim();
	}

	public void setUsst(String c) {
		this.usst = TransEncoding(c);
	}

	public String getCdno1() {
		return this.cdno1.trim();
	}

	public void setCdno1(String c) {
		this.cdno1 = TransEncoding(c);
	}

	public String getFg31() {
		return this.fg31.trim();
	}

	public void setFg31(String c) {
		this.fg31 = TransEncoding(c);
	}

	public String getEbno1() {
		return this.ebno1.trim();
	}

	public void setEbno1(String c) {
		this.ebno1 = TransEncoding(c);
	}

	public String getEbno2() {
		return this.ebno2.trim();
	}

	public void setEbno2(String c) {
		this.ebno2 = TransEncoding(c);
	}

	public String getEbno3() {
		return this.ebno3.trim();
	}

	public void setEbno3(String c) {
		this.ebno3 = TransEncoding(c);
	}

	public String getEbno4() {
		return this.ebno4.trim();
	}

	public void setEbno4(String c) {
		this.ebno4 = TransEncoding(c);
	}

	//D018
	public String getBitmap() {
		return this.bitmap.trim();
	}

	public void setBitmap(String c) {
		this.bitmap = TransEncoding(c);
	}

	public String getBag() {
		return this.bag.trim();
	}

	public void setBag(String c) {
		this.bag = TransEncoding(c);
	}

	//D020
	public int getGtcd() {
		return this.gtcd;
	}

	public void setGtcd(int c) {
		this.gtcd = c;
	}

	public String getGtdt() {
		return this.gtdt.trim();
	}

	public void setGtdt(String c) {
		this.gtdt = TransEncoding(c);
	}
	public void setGtdt(int c) {
		this.gtdt = String.valueOf(c);
	}

	public String getGtst() {
		return this.gtst.trim();
	}

	public void setGtst(String c) {
		this.gtst = TransEncoding(c);
	}

	public String getGts1() {
		return this.gts1.trim();
	}

	public void setGts1(String c) {
		this.gts1 = TransEncoding(c);
	}

	public String getTsfl() {
		return this.tsfl.trim();
	}

	public void setTsfl(String c) {
		this.tsfl = TransEncoding(c);
	}

	public String getWknt() {
		return this.wknt.trim();
	}

	public void setWknt(String c) {
		this.wknt = TransEncoding(c);
	}

	public String getUsid() {
		return this.usid.trim();
	}

	public void setUsid(String c) {
		this.usid = TransEncoding(c);
	}

	public String getOptp() {
		return this.optp.trim();
	}

	public void setOptp(String c) {
		this.optp = TransEncoding(c);
	}

	public double getApam() {
		return this.apam;
	}

	public void setApam(double c) {
		this.apam = c;
	}

	public String getBrus() {
		return this.brus.trim();
	}

	public void setBrus(String c) {
		this.brus = TransEncoding(c);
	}

	public String getBru1() {
		return this.bru1.trim();
	}

	public void setBru1(String c) {
		this.bru1 = TransEncoding(c);
	}

	public int getAgtm() {
		return this.agtm;
	}

	public void setAgtm(int c) {
		this.agtm = c;
	}

	public int getGttm() {
		return this.gttm;
	}

	public void setGttm(int c) {
		this.gttm = c;
	}

	public String getFg01() {
		return this.fg01.trim();
	}

	public void setFg01(String c) {
		this.fg01 = TransEncoding(c);
	}

	public String getFg09() {
		return this.fg09.trim();
	}

	public void setFg09(String c) {
		this.fg09 = TransEncoding(c);
	}

	public String getFg29() {
		return this.fg29.trim();
	}

	public void setFg29(String c) {
		this.fg29 = TransEncoding(c);
	}
	public String getAmt1() {
		return this.amt1.trim();
	}

	public void setAmt1(String c) {
		this.amt1 = TransEncoding(c);
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String c) {
		this.desc = TransEncoding(c);
	}

	//D021
	public int getQnum() {
		return this.qnum;
	}

	public void setQnum(int c) {
		this.qnum = c;
	}

	public double getQamt() {
		return this.qamt;
	}

	public void setQamt(double c) {
		this.qamt = c;
	}

	public int getCnum() {
		return this.cnum;
	}

	public void setCnum(int c) {
		this.cnum = c;
	}

	public double getCamt() {
		return this.camt;
	}

	public void setCamt(double c) {
		this.camt = c;
	}

	//Pfmt
	public int getFldseq() {
		return this.fldseq;
	}

	public void setFldseq(int c) {
		this.fldseq = c;
	}

	public int getFldfmt() {
		return this.fldfmt;
	}

	public void setFldfmt(int c) {
		this.fldfmt = c;
	}

	public String getFlddes() {
		return this.flddes.trim();
	}

	public void setFlddes(String c) {
		this.flddes = TransEncoding(c);
	}

	public String getFldnam() {
		return this.fldnam.trim();
	}

	public void setFldnam(String c) {
		this.fldnam = TransEncoding(c);
	}

	//Ptab
	public String getTabtyp() {
		return this.tabtyp.trim();
	}

	public void setTabtyp(String c) {
		this.tabtyp = TransEncoding(c);
	}

	public String getTablis() {
		return this.tablis.trim();
	}

	public void setTablis(String c) {
		this.tablis = TransEncoding(c);
	}

	public String getTabdes() {
		return this.tabdes.trim();
	}

	public void setTabdes(String c) {
		this.tabdes = TransEncoding(c);
	}

	public String getIdtpa() {
		return this.idtpa.trim();
	}

	public void setIdtpa(String c) {
		this.idtpa = TransEncoding(c);
	}

	public String getIdnoa() {
		return this.idnoa.trim();
	}

	public void setIdnoa(String c) {
		this.idnoa = TransEncoding(c);
	}

	public String getCusta() {
		return this.custa.trim();
	}

	public void setCusta(String c) {
		this.custa = TransEncoding(c);
	}

	public String getCunma() {
		return this.cunma.trim();
	}

	public void setCunma(String c) {
		this.cunma = TransEncoding(c);
	}

	public String getCunma1() {
		return this.cunma1.trim();
	}

	public void setCunma1(String c) {
		this.cunma1 = TransEncoding(c);
	}

	//P001

	public String getName() {
		return this.name.trim();
	}

	public void setName(String c) {
		this.name = TransEncoding(c);
	}

	public String getGrop() {
		return this.grop.trim();
	}

	public void setGrop(String c) {
		this.grop = TransEncoding(c);
	}

	public String getPgm() {
		return this.pgm.trim();
	}

	public void setPgm(String c) {
		this.pgm = TransEncoding(c);
	}

	public String getFlg1() {
		return this.flg1.trim();
	}

	public void setFlg1(String c) {
		this.flg1 = TransEncoding(c);
	}

	public String getFlg6() {
		return this.flg6.trim();
	}

	public void setFlg6(String c) {
		this.flg6 = TransEncoding(c);
	}

	public String getFlg7() {
		return this.flg7.trim();
	}

	public void setFlg7(String c) {
		this.flg7 = TransEncoding(c);
	}

	public String getFlg8() {
		return this.flg8.trim();
	}

	public void setFlg8(String c) {
		this.flg8 = TransEncoding(c);
	}

	public String getAcod() {
		return this.acod.trim();
	}

	public void setAcod(String c) {
		this.acod = TransEncoding(c);
	}

	public String getCyck() {
		return this.cyck.trim();
	}

	public void setCyck(String c) {
		this.cyck = TransEncoding(c);
	}

	public String getMttp() {
		return this.mttp.trim();
	}

	public void setMttp(String c) {
		this.mttp = TransEncoding(c);
	}

	public String getDbag() {
		return this.dbag.trim();
	}

	public void setDbag(String c) {
		this.dbag = TransEncoding(c);
	}

	public String getRem() {
		return this.rem.trim();
	}

	public void setRem(String c) {
		this.rem = TransEncoding(c);
	}

	//P002
	public String getPocudt() {
		return this.pocudt.trim();
	}

	public void setPocudt(String c) {
		this.pocudt = TransEncoding(c);
	}
	public void setPocudt(int c) {
		this.pocudt = String.valueOf(c);
	}

	public String getPoltdt() {
		return this.poltdt.trim();
	}

	public void setPoltdt(String c) {
		this.poltdt = TransEncoding(c);
	}
	public void setPoltdt(int c) {
		this.poltdt = String.valueOf(c);
	}

	public String getPonxdt() {
		return this.ponxdt.trim();
	}

	public void setPonxdt(String c) {
		this.ponxdt = TransEncoding(c);
	}
	public void setPonxdt(int c) {
		this.ponxdt = String.valueOf(c);
	}

	public String getPosts() {
		return this.posts.trim();
	}

	public void setPosts(String c) {
		this.posts = TransEncoding(c);
	}

	public String getPodofw() {
		return this.podofw.trim();
	}

	public void setPodofw(String c) {
		this.podofw = TransEncoding(c);
	}

	public String getPowfef() {
		return this.powfef.trim();
	}

	public void setPowfef(String c) {
		this.powfef = TransEncoding(c);
	}

	public String getPow25f() {
		return this.pow25f.trim();
	}

	public void setPow25f(String c) {
		this.pow25f = TransEncoding(c);
	}

	public String getPowwef() {
		return this.powwef.trim();
	}

	public void setPowwef(String c) {
		this.powwef = TransEncoding(c);
	}

	public String getPowtef() {
		return this.powtef.trim();
	}

	public void setPowtef(String c) {
		this.powtef = TransEncoding(c);
	}

	public String getPowmef() {
		return this.powmef.trim();
	}

	public void setPowmef(String c) {
		this.powmef = TransEncoding(c);
	}

	public String getPowqef() {
		return this.powqef.trim();
	}

	public void setPowqef(String c) {
		this.powqef = TransEncoding(c);
	}

	public String getPowhef() {
		return this.powhef.trim();
	}

	public void setPowhef(String c) {
		this.powhef = TransEncoding(c);
	}

	public String getPowyef() {
		return this.powyef.trim();
	}

	public void setPowyef(String c) {
		this.powyef = TransEncoding(c);
	}

	public String getPowybf() {
		return this.powybf.trim();
	}

	public void setPowybf(String c) {
		this.powybf = TransEncoding(c);
	}

	public int getThtm() {
		return this.thtm;
	}

	public void setThtm(int c) {
		this.thtm = c;
	}

	public int getLstm() {
		return this.lstm;
	}

	public void setLstm(int c) {
		this.lstm = c;
	}

	//P003

	public String getAtnm() {
		return this.atnm.trim();
	}

	public void setAtnm(String c) {
		this.atnm = TransEncoding(c);
	}

	public String getItat() {
		return this.itat.trim();
	}

	public void setItat(String c) {
		this.itat = TransEncoding(c);
	}

	public int getItlv() {
		return this.itlv;
	}

	public void setItlv(int c) {
		this.itlv = c;
	}

	public String getSust() {
		return this.sust.trim();
	}

	public void setSust(String c) {
		this.sust = TransEncoding(c);
	}

	public String getRpst() {
		return this.rpst.trim();
	}

	public void setRpst(String c) {
		this.rpst = TransEncoding(c);
	}

	public String getTxtp() {
		return this.txtp.trim();
	}

	public void setTxtp(String c) {
		this.txtp = TransEncoding(c);
	}

	public String getInacit() {
		return this.inacit.trim();
	}

	public void setInacit(String c) {
		this.inacit = TransEncoding(c);
	}

	public String getInacit1() {
		return this.inacit1.trim();
	}

	public void setInacit1(String c) {
		this.inacit1 = TransEncoding(c);
	}

	public String getInacit2() {
		return this.inacit2.trim();
	}

	public void setInacit2(String c) {
		this.inacit2 = TransEncoding(c);
	}

	public String getAcgp() {
		return this.acgp.trim();
	}

	public void setAcgp(String c) {
		this.acgp = TransEncoding(c);
	}

	public String getRedf() {
		return this.redf.trim();
	}

	public void setRedf(String c) {
		this.redf = TransEncoding(c);
	}

	public String getIctp() {
		return this.ictp.trim();
	}

	public void setIctp(String c) {
		this.ictp = TransEncoding(c);
	}

	public String getSmst() {
		return this.smst.trim();
	}

	public void setSmst(String c) {
		this.smst = TransEncoding(c);
	}

	public String getTbcl() {
		return this.tbcl.trim();
	}

	public void setTbcl(String c) {
		this.tbcl = TransEncoding(c);
	}

	public String getOcst() {
		return this.ocst.trim();
	}

	public void setOcst(String c) {
		this.ocst = TransEncoding(c);
	}

	public String getCkfg1() {
		return this.ckfg1.trim();
	}

	public void setCkfg1(String c) {
		this.ckfg1 = TransEncoding(c);
	}

	public int getCdnum1() {
		return this.cdnum1;
	}

	public void setCdnum1(int c) {
		this.cdnum1 = c;
	}

	public int getCdnum2() {
		return this.cdnum2;
	}

	public void setCdnum2(int c) {
		this.cdnum2 = c;
	}

	public int getCdnum3() {
		return this.cdnum3;
	}

	public void setCdnum3(int c) {
		this.cdnum3 = c;
	}

	public String getOafg() {
		return this.oafg.trim();
	}

	public void setOafg(String c) {
		this.oafg = TransEncoding(c);
	}

	//P004
	public String getCycd() {
		return this.cycd.trim();
	}

	public void setCycd(String c) {
		this.cycd = TransEncoding(c);
	}

	public String getRgcd() {
		return this.rgcd.trim();
	}

	public void setRgcd(String c) {
		this.rgcd = TransEncoding(c);
	}

	public int getExut() {
		return this.exut;
	}

	public void setExut(int c) {
		this.exut = c;
	}

	public double getCuby() {
		return this.cuby;
	}

	public void setCuby(double c) {
		this.cuby = c;
	}

	public double getCusl() {
		return this.cusl;
	}

	public void setCusl(double c) {
		this.cusl = c;
	}

	public double getMeby() {
		return this.meby;
	}

	public void setMeby(double c) {
		this.meby = c;
	}

	public double getMesl() {
		return this.mesl;
	}

	public void setMesl(double c) {
		this.mesl = c;
	}

	public double getMdpr() {
		return this.mdpr;
	}

	public void setMdpr(double c) {
		this.mdpr = c;
	}

	public double getEnpr() {
		return this.enpr;
	}

	public void setEnpr(double c) {
		this.enpr = c;
	}

	public int getSbut() {
		return this.sbut;
	}

	public void setSbut(int c) {
		this.sbut = c;
	}

	public String getEddt() {
		return this.eddt.trim();
	}

	public void setEddt(String c) {
		this.eddt = TransEncoding(c);
	}
	public void setEddt(int c) {
		this.eddt = String.valueOf(c);
	}

	public String getStcd() {
		return this.stcd.trim();
	}

	public void setStcd(String c) {
		this.stcd = TransEncoding(c);
	}

	//P005
	public String getSvcd() {
		return this.svcd.trim();
	}

	public void setSvcd(String c) {
		this.svcd = TransEncoding(c);
	}

	public String getSvcd1() {
		return this.svcd1.trim();
	}

	public void setSvcd1(String c) {
		this.svcd1= TransEncoding(c);
	}
	public String getSvcd2() {
		return this.svcd2.trim();
	}

	public void setSvcd2(String c) {
		this.svcd2= TransEncoding(c);
	}

	public int getSeq() {
		return this.seq;
	}

	public void setSeq(int c) {
		this.seq = c;
	}

	public String getIoff() {
		return this.ioff.trim();
	}

	public void setIoff(String c) {
		this.ioff = TransEncoding(c);
	}

	public String getCtff() {
		return this.ctff.trim();
	}

	public void setCtff(String c) {
		this.ctff = TransEncoding(c);
	}

	public String getAccf() {
		return this.accf.trim();
	}

	public void setAccf(String c) {
		this.accf = TransEncoding(c);
	}

	public String getAmcf() {
		return this.amcf.trim();
	}

	public void setAmcf(String c) {
		this.amcf = TransEncoding(c);
	}

	public String getTscf() {
		return this.tscf.trim();
	}

	public void setTscf(String c) {
		this.tscf = TransEncoding(c);
	}

	public String getCynf() {
		return this.cynf.trim();
	}

	public void setCynf(String c) {
		this.cynf = TransEncoding(c);
	}

	public String getTsaf() {
		return this.tsaf.trim();
	}

	public void setTsaf(String c) {
		this.tsaf = TransEncoding(c);
	}

	public String getCdtf() {
		return this.cdtf.trim();
	}

	public void setCdtf(String c) {
		this.cdtf = TransEncoding(c);
	}

	public String getCdnf() {
		return this.cdnf.trim();
	}

	public void setCdnf(String c) {
		this.cdnf = TransEncoding(c);
	}

	public String getOitf() {
		return this.oitf.trim();
	}

	public void setOitf(String c) {
		this.oitf = TransEncoding(c);
	}

	public String getPtcf() {
		return this.ptcf.trim();
	}

	public void setPtcf(String c) {
		this.ptcf = TransEncoding(c);
	}

	public String getLbcf() {
		return this.lbcf.trim();
	}

	public void setLbcf(String c) {
		this.lbcf = TransEncoding(c);
	}

	public String getVcsf() {
		return this.vcsf.trim();
	}

	public void setVcsf(String c) {
		this.vcsf = TransEncoding(c);
	}

	public String getSpec() {
		return this.spec.trim();
	}

	public void setSpec(String c) {
		this.spec = TransEncoding(c);
	}

	public String getCuss() {
		return this.cuss.trim();
	}

	public void setCuss(String c) {
		this.cuss = TransEncoding(c);
	}

	public String getAccs() {
		return this.accs.trim();
	}

	public void setAccs(String c) {
		this.accs = TransEncoding(c);
	}

	public String getCdts() {
		return this.cdts.trim();
	}

	public void setCdts(String c) {
		this.cdts = TransEncoding(c);
	}

	//P006

	public double getMxam() {
		return this.mxam;
	}

	public void setMxam(double c) {
		this.mxam = c;
	}

	public double getOpam() {
		return this.opam;
	}

	public void setOpam(double c) {
		this.opam = c;
	}

	public double getRmam() {
		return this.rmam;
	}

	public void setRmam(double c) {
		this.rmam = c;
	}

	public double getStam() {
		return this.stam;
	}

	public void setStam(double c) {
		this.stam = c;
	}

	public double getTtam() {
		return this.ttam;
	}

	public void setTtam(double c) {
		this.ttam = c;
	}

	public int getCrct() {
		return this.crct;
	}

	public void setCrct(int c) {
		this.crct = c;
	}

	public int getDrct() {
		return this.drct;
	}

	public void setDrct(int c) {
		this.drct = c;
	}

	//P007
	public String getUsnm() {
		return this.usnm.trim();
	}

	public void setUsnm(String c) {
		this.usnm = TransEncoding(c);
	}

	public String getUstp() {
		return this.ustp.trim();
	}

	public void setUstp(String c) {
		this.ustp = TransEncoding(c);
	}

	public String getUsbr() {
		return this.usbr.trim();
	}

	public void setUsbr(String c) {
		this.usbr = TransEncoding(c);
	}

	public String getUsgp() {
		return this.usgp.trim();
	}

	public void setUsgp(String c) {
		this.usgp = TransEncoding(c);
	}

	public String getCvcd() {
		return this.cvcd.trim();
	}

	public void setCvcd(String c) {
		this.cvcd = TransEncoding(c);
	}

	public String getUspw() {
		return this.uspw.trim();
	}

	public void setUspw(String c) {
		this.uspw = TransEncoding(c);
	}

	public String getUsdt() {
		return this.usdt.trim();
	}

	public void setUsdt(String c) {
		this.usdt = TransEncoding(c);
	}
	public void setUsdt(int c) {
		this.usdt = String.valueOf(c);
	}

	public String getTmid() {
		return this.tmid.trim();
	}

	public void setTmid(String c) {
		this.tmid = TransEncoding(c);
	}

	public String getTmid1() {
		return this.tmid1.trim();
	}

	public void setTmid1(String c) {
		this.tmid1= TransEncoding(c);
	}

	public String getTmid2() {
		return this.tmid2.trim();
	}

	public void setTmid2(String c) {
		this.tmid2= TransEncoding(c);
	}

	public String getTmid3() {
		return this.tmid3.trim();
	}

	public void setTmid3(String c) {
		this.tmid3= TransEncoding(c);
	}


	public String getZmno() {
		return this.zmno.trim();
	}

	public void setZmno(String c) {
		this.zmno = TransEncoding(c);
	}

	public String getBast() {
		return this.bast.trim();
	}

	public void setBast(String c) {
		this.bast = TransEncoding(c);
	}

	public String getFg14() {
		return this.fg14.trim();
	}

	public void setFg14(String c) {
		this.fg14 = TransEncoding(c);
	}

	public String getPgdr() {
		return this.pgdr.trim();
	}

	public void setPgdr(String c) {
		this.pgdr = TransEncoding(c);
	}

	public String getTsusa() {
		return this.tsusa.trim();
	}

	public void setTsusa(String c) {
		this.tsusa = TransEncoding(c);
	}

	public String getOpbra() {
		return this.opbra.trim();
	}

	public void setOpbra(String c) {
		this.opbra = TransEncoding(c);
	}

	public String getOtbr() {
		return this.otbr.trim();
	}

	public void setOtbr(String c) {
		this.otbr = TransEncoding(c);
	}

	public String getOpdtac() {
		return this.opdtac.trim();
	}

	public void setOpdtac(String c) {
		this.opdtac = TransEncoding(c);
	}

	public String getFrdtac() {
		return this.frdtac.trim();
	}

	public void setFrdtac(String c) {
		this.frdtac = TransEncoding(c);
	}
	public String getFrtpa() {
		return this.frtpa.trim();
	}

	public void setFrtpa(String c) {
		this.frtpa = TransEncoding(c);
	}

	public String getTsb2() {
		return this.tsb2.trim();
	}

	public void setTsb2(String c) {
		this.tsb2 = TransEncoding(c);
	}

	public String getNumac1() {
		return this.numac1.trim();
	}

	public void setNumac1(String c) {
		this.numac1 = TransEncoding(c);
	}

	public String getSrnoa() {
		return this.srnoa.trim();
	}

	public void setSrnoa(String c) {
		this.srnoa = TransEncoding(c);
	}

	public String getFg20a() {
		return this.fg20a.trim();
	}

	public void setFg20a(String c) {
		this.fg20a = TransEncoding(c);
	}

	public String getFg21a() {
		return this.fg21a.trim();
	}

	public void setFg21a(String c) {
		this.fg21a = TransEncoding(c);
	}

	public String getFg22a() {
		return this.fg22a.trim();
	}

	public void setFg22a(String c) {
		this.fg22a = TransEncoding(c);
	}


	public String getBbr1a() {
		return this.bbr1a.trim();
	}

	public void setBbr1a(String c) {
		this.bbr1a = TransEncoding(c);
	}

	public String getPocda() {
		return this.pocda.trim();
	}

	public void setPocda(String c) {
		this.pocda = TransEncoding(c);
	}

	public String getCuno1() {
		return this.cuno1.trim();
	}

	public void setCuno1(String c) {
		this.cuno1 = TransEncoding(c);
	}

	public String getCuno2() {
		return this.cuno2.trim();
	}

	public void setCuno2(String c) {
		this.cuno2 = TransEncoding(c);
	}

	public String getCuno3() {
		return this.cuno3.trim();
	}

	public void setCuno3(String c) {
		this.cuno3 = TransEncoding(c);
	}


	public String getCuno4() {
		return this.cuno4.trim();
	}

	public void setCuno4(String c) {
		this.cuno4 = TransEncoding(c);
	}


	public String getCuno5() {
		return this.cuno5.trim();
	}

	public void setCuno5(String c) {
		this.cuno5 = TransEncoding(c);
	}

	public String getCuno6() {
		return this.cuno6.trim();
	}

	public void setCuno6(String c) {
		this.cuno6 = TransEncoding(c);
	}

	public String getCuno7() {
		return this.cuno7.trim();
	}

	public void setCuno7(String c) {
		this.cuno7 = TransEncoding(c);
	}

	public String getCuno8() {
		return this.cuno8.trim();
	}

	public void setCuno8(String c) {
		this.cuno8 = TransEncoding(c);
	}

	public String getCuno9() {
		return this.cuno9.trim();
	}

	public void setCuno9(String c) {
		this.cuno9 = TransEncoding(c);
	}

	public String getCuno10() {
		return this.cuno10.trim();
	}

	public void setCuno10(String c) {
		this.cuno10 = TransEncoding(c);
	}

	public String getCuno11() {
		return this.cuno11.trim();
	}

	public void setCuno11(String c) {
		this.cuno11 = TransEncoding(c);
	}

	public String getCuno12() {
		return this.cuno12.trim();
	}

	public void setCuno12(String c) {
		this.cuno12 = TransEncoding(c);
	}

	//P009
	public String getBrno() {
		return this.brno.trim();
	}

	public void setBrno(String c) {
		this.brno = TransEncoding(c);
	}

	public String getBrnm() {
		return this.brnm.trim();
	}

	public void setBrnm(String c) {
		this.brnm = TransEncoding(c);
	}

	public String getBrsn() {
		return this.brsn.trim();
	}

	public void setBrsn(String c) {
		this.brsn = TransEncoding(c);
	}

	public String getUpbr() {
		return this.upbr.trim();
	}

	public void setUpbr(String c) {
		this.upbr = TransEncoding(c);
	}

	public String getUpb1() {
		return this.upb1.trim();
	}

	public void setUpb1(String c) {
		this.upb1 = TransEncoding(c);
	}

	public String getExlg() {
		return this.exlg.trim();
	}

	public void setExlg(String c) {
		this.exlg = TransEncoding(c);
	}

	//P010

	public String getEbnm() {
		return this.ebnm.trim();
	}

	public void setEbnm(String c) {
		this.ebnm = TransEncoding(c);
	}

	public String getEbst() {
		return this.ebst.trim();
	}

	public void setEbst(String c) {
		this.ebst = TransEncoding(c);
	}

	public String getEblv() {
		return this.eblv.trim();
	}

	public void setEblv(String c) {
		this.eblv = TransEncoding(c);
	}

	//P011
	public String getOpt1() {
		return this.opt1.trim();
	}

	public void setOpt1(String c) {
		this.opt1 = TransEncoding(c);
	}

	public String getBkno() {
		return this.bkno.trim();
	}

	public void setBkno(String c) {
		this.bkno = TransEncoding(c);
	}

	public String getAcct3() {
		return this.acct3.trim();
	}

	public void setAcct3(String c) {
		this.acct3 = TransEncoding(c);
	}

	public String getAcct4() {
		return this.acct4.trim();
	}

	public void setAcct4(String c) {
		this.acct4 = TransEncoding(c);
	}

	public String getAcct5() {
		return this.acct5.trim();
	}

	public void setAcct5(String c) {
		this.acct5 = TransEncoding(c);
	}

	public String getAcct6() {
		return this.acct6.trim();
	}

	public void setAcct6(String c) {
		this.acct6 = TransEncoding(c);
	}

	public String getAcct7() {
		return this.acct7.trim();
	}

	public void setAcct7(String c) {
		this.acct7 = TransEncoding(c);
	}

	public String getAcct8() {
		return this.acct8.trim();
	}

	public void setAcct8(String c) {
		this.acct8 = TransEncoding(c);
	}

	public String getAcct9() {
		return this.acct9.trim();
	}

	public void setAcct9(String c) {
		this.acct9 = TransEncoding(c);
	}

	public String getAcct10() {
		return this.acct10.trim();
	}

	public void setAcct10(String c) {
		this.acct10 = TransEncoding(c);
	}

	public String getAcct11() {
		return this.acct11.trim();
	}

	public void setAcct11(String c) {
		this.acct11 = TransEncoding(c);
	}

	public String getAcct12() {
		return this.acct12.trim();
	}

	public void setAcct12(String c) {
		this.acct12 = TransEncoding(c);
	}

	//P012
	public double getVal() {
		return this.val;
	}

	public void setVal(double c) {
		this.val = c;
	}

	public double getF01() {
		return this.f01;
	}

	public void setF01(double c) {
		this.f01 = c;
	}

	public String getCdno3() {
		return this.cdno3.trim();
	}

	public void setCdno3(String c) {
		this.cdno3 = TransEncoding(c);
	}

	public String getAcsta() {
		return this.acsta.trim();
	}

	public void setAcsta(String c) {
		this.acsta = TransEncoding(c);
	}

	public String getAcctac() {
		return this.acctac.trim();
	}

	public void setAcctac(String c) {
		this.acctac = TransEncoding(c);
	}

	public String getTscda() {
		return this.tscda.trim();
	}

	public void setTscda(String c) {
		this.tscda = TransEncoding(c);
	}

	public String getSucda() {
		return this.sucda.trim();
	}

	public void setSucda(String c) {
		this.sucda = TransEncoding(c);
	}

	public String getTssta() {
		return this.tssta.trim();
	}

	public void setTssta(String c) {
		this.tssta = TransEncoding(c);
	}

	public String getNumac2() {
		return this.numac2.trim();
	}

	public void setNumac2(String c) {
		this.numac2 = TransEncoding(c);
	}

	public String getDramac() {
		return this.dramac.trim();
	}

	public void setDramac(String c) {
		this.dramac = TransEncoding(c);
	}

	public String getCramac() {
		return this.cramac.trim();
	}

	public void setCramac(String c) {
		this.cramac = TransEncoding(c);
	}

	public String getDrblac() {
		return this.drblac.trim();
	}

	public void setDrblac(String c) {
		this.drblac = TransEncoding(c);
	}

	public String getCrblac() {
		return this.crblac.trim();
	}

	public void setCrblac(String c) {
		this.crblac = TransEncoding(c);
	}

	public String getName1() {
		return this.name1.trim();
	}

	public void setName1(String c) {
		this.name1 = TransEncoding(c);
	}

	public String getLobl() {
		return this.lobl.trim();
	}

	public void setLobl(String c) {
		this.lobl = TransEncoding(c);
	}

	public float getXdra() {
		return this.xdra;
	}

	public void setXdra(float c) {
		this.xdra = c;
	}

	public float getRat1() {
		return this.rat1;
	}

	public void setRat1(float c) {
		this.rat1 = c;
	}

	public float getRat2() {
		return this.rat2;
	}

	public void setRat2(float c) {
		this.rat2 = c;
	}

	public float getRat3() {
		return this.rat3;
	}

	public void setRat3(float c) {
		this.rat3 = c;
	}

	public float getRat4() {
		return this.rat4;
	}

	public void setRat4(float c) {
		this.rat4 = c;
	}

	public float getRat11() {
		return this.rat11;
	}

	public void setRat11(float c) {
		this.rat11 = c;
	}

	public float getRat21() {
		return this.rat21;
	}

	public void setRat21(float c) {
		this.rat21 = c;
	}


	public float getRat31() {
		return this.rat31;
	}

	public void setRat31(float c) {
		this.rat31 = c;
	}


	public float getRat41() {
		return this.rat41;
	}

	public void setRat41(float c) {
		this.rat41 = c;
	}


	public float getRat51() {
		return this.rat51;
	}

	public void setRat51(float c) {
		this.rat51 = c;
	}

	public double getFram() {
		return this.fram;
	}

	public void setFram(double c) {
		this.fram = c;
	}

	public String getName2() {
		return this.name2.trim();
	}

	public void setName2(String c) {
		this.name2 = TransEncoding(c);
	}

	public String getName3() {
		return this.name3.trim();
	}

	public void setName3(String c) {
		this.name3 = TransEncoding(c);
	}

	public String getName4() {
		return this.name4.trim();
	}

	public void setName4(String c) {
		this.name4 = TransEncoding(c);
	}

	public String getName5() {
		return this.name5.trim();
	}

	public void setName5(String c) {
		this.name5 = TransEncoding(c);
	}
	public String getAcit1() {
		return this.acit1.trim();
	}

	public void setAcit1(String c) {
		this.acit1 = TransEncoding(c);
	}

	public String getAcit2() {
		return this.acit2.trim();
	}

	public void setAcit2(String c) {
		this.acit2 = TransEncoding(c);
	}

	public String getAcit3() {
		return this.acit3.trim();
	}

	public void setAcit3(String c) {
		this.acit3 = TransEncoding(c);
	}

	public String getAcit4() {
		return this.acit4.trim();
	}

	public void setAcit4(String c) {
		this.acit4 = TransEncoding(c);
	}

	public String getEacd() {
		return this.eacd.trim();
	}

	public void setEacd(String c) {
		this.eacd = TransEncoding(c);
	}
	public String getFg16() {
		return this.fg16.trim();
	}

	public void setFg16(String c) {
		this.fg16 = TransEncoding(c);
	}
	public String getIntp1() {
		return this.intp1.trim();
	}

	public void setIntp1(String c) {
		this.intp1 = TransEncoding(c);
	}
	public double getDram() {
		return this.dram;
	}

	public void setDram(double c) {
		this.dram=c;
	}
	public double getTsam5() {
		return this.tsam5;
	}

	public void setTsam5(double c) {
		this.tsam5 = c;
	}

	public double getTsam6() {
		return this.tsam6;
	}

	public void setTsam6(double c) {
		this.tsam6 = c;
	}

	public double getTsam7() {
		return this.tsam7;
	}

	public void setTsam7(double c) {
		this.tsam7 = c;
	}

	public double getTsam8() {
		return this.tsam8;
	}

	public void setTsam8(double c) {
		this.tsam8 = c;
	}

	public double getTsam9() {
		return this.tsam9;
	}

	public void setTsam9(double c) {
		this.tsam9 = c;
	}

	public double getTsam10() {
		return this.tsam10;
	}

	public void setTsam10(double c) {
		this.tsam10 = c;
	}

	public double getTsam11() {
		return this.tsam11;
	}

	public void setTsam11(double c) {
		this.tsam11 = c;
	}

	//P013
	public String getKywd() {
		return this.kywd.trim();
	}

	public void setKywd(String c) {
		this.kywd = TransEncoding(c);
	}

	//P015

	public String getCist() {
		return this.cist.trim();
	}

	public void setCist(String c) {
		this.cist = TransEncoding(c);
	}

	public String getVabr() {
		return this.vabr.trim();
	}

	public void setVabr(String c) {
		this.vabr = TransEncoding(c);
	}

	//P016
	public String getInfr() {
		return this.infr.trim();
	}

	public void setInfr(String c) {
		this.infr = TransEncoding(c);
	}

	public String getScdt() {
		return this.scdt.trim();
	}

	public void setScdt(String c) {
		this.scdt = TransEncoding(c);
	}
	public void setScdt(int c) {
		this.scdt = String.valueOf(c);
	}

	//P017
	public String getInpm() {
		return this.inpm.trim();
	}

	public void setInpm(String c) {
		this.inpm = TransEncoding(c);
	}

	public String getCdno4() {
		return this.cdno4.trim();
	}

	public void setCdno4(String c) {
		this.cdno4 = TransEncoding(c);
	}

	public String getCdno5() {
		return this.cdno5.trim();
	}

	public void setCdno5(String c) {
		this.cdno5 = TransEncoding(c);
	}

	public String getCdno6() {
		return this.cdno6.trim();
	}

	public void setCdno6(String c) {
		this.cdno6 = TransEncoding(c);
	}

	public String getCdno7() {
		return this.cdno7.trim();
	}

	public void setCdno7(String c) {
		this.cdno7 = TransEncoding(c);
	}

	public String getCdno8() {
		return this.cdno8.trim();
	}

	public void setCdno8(String c) {
		this.cdno8 = TransEncoding(c);
	}

	public String getCdno9() {
		return this.cdno9.trim();
	}

	public void setCdno9(String c) {
		this.cdno9 = TransEncoding(c);
	}

	public String getCdno10() {
		return this.cdno10.trim();
	}

	public void setCdno10(String c) {
		this.cdno10 = TransEncoding(c);
	}

	public String getCdno11() {
		return this.cdno11.trim();
	}

	public void setCdno11(String c) {
		this.cdno11 = TransEncoding(c);
	}

	public String getCdno12() {
		return this.cdno12.trim();
	}

	public void setCdno12(String c) {
		this.cdno12 = TransEncoding(c);
	}

	//P019
	public String getPmtp() {
		return this.pmtp.trim();
	}

	public void setPmtp(String c) {
		this.pmtp = TransEncoding(c);
	}

	public String getPmky() {
		return this.pmky.trim();
	}

	public void setPmky(String c) {
		this.pmky = TransEncoding(c);
	}

	public String getPmcd() {
		return this.pmcd.trim();
	}

	public void setPmcd(String c) {
		this.pmcd = TransEncoding(c);
	}

	public String getPmnm() {
		return this.pmnm.trim();
	}

	public void setPmnm(String c) {
		this.pmnm = TransEncoding(c);
	}

	public String getPmv1() {
		return this.pmv1.trim();
	}

	public void setPmv1(String c) {
		this.pmv1 = TransEncoding(c);
	}

	public String getPmv2() {
		return this.pmv2.trim();
	}

	public void setPmv2(String c) {
		this.pmv2 = TransEncoding(c);
	}

	public String getPmv3() {
		return this.pmv3.trim();
	}

	public void setPmv3(String c) {
		this.pmv3 = TransEncoding(c);
	}

	public String getPmv4() {
		return this.pmv4.trim();
	}

	public void setPmv4(String c) {
		this.pmv4 = TransEncoding(c);
	}

	public String getPmv5() {
		return this.pmv5.trim();
	}

	public void setPmv5(String c) {
		this.pmv5 = TransEncoding(c);
	}

	//V007
	public String getFg12() {
		return this.fg12.trim();
	}

	public void setFg12(String c) {
		this.fg12 = TransEncoding(c);
	}

	//V008
	public String getRptp() {
		return this.rptp.trim();
	}

	public void setRptp(String c) {
		this.rptp = TransEncoding(c);
	}

	public int getCoun() {
		return this.coun;
	}

	public void setCoun(int c) {
		this.coun = c;
	}

	public String getOuus() {
		return this.ouus.trim();
	}

	public void setOuus(String c) {
		this.ouus = TransEncoding(c);
	}

	//P021
	public String getTyp() {
		return this.typ.trim();
	}

	public void setTyp(String c) {
		this.typ = TransEncoding(c);
	}

	public String getDpt() {
		return this.dpt.trim();
	}

	public void setDpt(String c) {
		this.dpt = TransEncoding(c);
	}

	public int getInc() {
		return this.inc;
	}

	public void setInc(int c) {
		this.inc = c;
	}

	public int getIni() {
		return this.ini;
	}

	public void setIni(int c) {
		this.ini = c;
	}

	public int getMax() {
		return this.max;
	}

	public void setMax(int c) {
		this.max = c;
	}

	//P027

	public String getAutp() {
		return this.autp.trim();
	}

	public void setAutp(String c) {
		this.autp = TransEncoding(c);
	}

	public double getQuta() {
		return this.quta;
	}

	public void setQuta(double c) {
		this.quta = c;
	}

	public double getQutt() {
		return this.qutt;
	}

	public void setQutt(double c) {
		this.qutt = c;
	}

	public String getAufg() {
		return this.aufg.trim();
	}

	public void setAufg(String c) {
		this.aufg = TransEncoding(c);
	}

	public String getStdt() {
		return this.stdt.trim();
	}

	public void setStdt(String c) {
		this.stdt = TransEncoding(c);
	}
	public void setStdt(int c) {
		this.stdt = String.valueOf(c);
	}

	public String getPlac() {
		return this.plac.trim();
	}

	public void setPlac(String c) {
		this.plac = TransEncoding(c);
	}

	//P028

	public String getBktp() {
		return this.bktp.trim();
	}

	public void setBktp(String c) {
		this.bktp = TransEncoding(c);
	}

	public String getBksn() {
		return this.bksn.trim();
	}

	public void setBksn(String c) {
		this.bksn = TransEncoding(c);
	}

	public String getBknm() {
		return this.bknm.trim();
	}

	public void setBknm(String c) {
		this.bknm = TransEncoding(c);
	}

	public String getBkad() {
		return this.bkad.trim();
	}

	public void setBkad(String c) {
		this.bkad = TransEncoding(c);
	}

	public String getBktl() {
		return this.bktl.trim();
	}

	public void setBktl(String c) {
		this.bktl = TransEncoding(c);
	}

	public String getBkfx() {
		return this.bkfx.trim();
	}

	public void setBkfx(String c) {
		this.bkfx = TransEncoding(c);
	}

	//P030
	public String getVon() {
		return this.von.trim();
	}

	public void setVon(String c) {
		this.von = TransEncoding(c);
	}

	public String getVons() {
		return this.vons.trim();
	}

	public void setVons(String c) {
		this.vons = TransEncoding(c);
	}

	public int getSttm() {
		return this.sttm;
	}

	public void setSttm(int c) {
		this.sttm = c;
	}

	public int getDutm() {
		return this.dutm;
	}

	public void setDutm(int c) {
		this.dutm = c;
	}

	public String getSts() {
		return this.sts.trim();
	}

	public void setSts(String c) {
		this.sts = TransEncoding(c);
	}

	public String getCucd() {
		return this.cucd.trim();
	}

	public void setCucd(String c) {
		this.cucd = TransEncoding(c);
	}

	public String getOftl() {
		return this.oftl.trim();
	}

	public void setOftl(String c) {
		this.oftl = c;
	}

	public String getFg17() {
		return this.fg17.trim();
	}

	public void setFg17(String c) {
		this.fg17 = c;
	}

	public String getOtbk() {
		return this.otbk.trim();
	}

	public void setOtbk(String c) {
		this.otbk = c;
	}

	public double getTsam12() {
		return this.tsam12;
	}

	public void setTsam12(double c) {
		this.tsam12 = c;
	}
	public String getNumac3() {
		return this.numac3.trim();
	}

	public void setNumac3(String c) {
		this.numac3 = TransEncoding(c);
	}
	public String getAcsf() {
		return this.acsf.trim();
	}

	public void setAcsf(String c) {
		this.acsf = TransEncoding(c);
	}
	public String getAcsf1() {
		return this.acsf1.trim();
	}

	public void setAcsf1(String c) {
		this.acsf1= TransEncoding(c);
	}
	public String getAcsf2() {
		return this.acsf2.trim();
	}

	public void setAcsf2(String c) {
		this.acsf2= TransEncoding(c);
	}
	public String getAcsfa() {
		return this.acsfa.trim();
	}

	public void setAcsfa(String c) {
		this.acsfa = TransEncoding(c);
	}
	public String getCdno2() {
		return this.cdno2.trim();
	}

	public void setCdno2(String c) {
		this.cdno2 = TransEncoding(c);
	}
	public String getFesd() {
		return this.fesd.trim();
	}

	public void setFesd(String c) {
		this.fesd = TransEncoding(c);
	}

	public String getFeed() {
		return this.feed.trim();
	}

	public void setFeed(String c) {
		this.feed = TransEncoding(c);
	}

	public String getFbuf() {
		return this.fbuf.trim();
	}

	public void setFbuf(String c) {
		this.fbuf = TransEncoding(c);
	}

	//Testjava
	public String getTablename()
	{
		return this.tablename.trim();
	}
	public void setTablename(String c)
	{
		this.tablename=TransEncoding(c);
	}


	public String getZdname()
	{
		return this.zdname.trim();
	}
	public void setZdname(String c)
	{
		this.zdname=TransEncoding(c);
	}


	public String getZddes()
	{
		return this.zddes.trim();
	}
	public void setZddes(String c)
	{
		this.zddes=TransEncoding(c);
	}


	public String getZdremark()
	{
		return this.zdremark.trim();
	}
	public void setZdremark(String c)
	{
		this.zdremark=TransEncoding(c);
	}
	public String getDbnm()
	{
		return this.dbnm.trim();
	}
	public void setDbnm(String c)
	{
		if	(c==null) c="";
		this.dbnm=c.trim();
	}


	public byte[] getPin()
	{
		return this.pin;
	}
	public void setPin(byte[] c)
	{
		if	(c.length>0)	System.arraycopy(c, 0, pin, 0, (c.length>pin.length?pin.length:c.length));
	}

	public byte[] getPin1()
	{
		return this.pin1;
	}
	public void setPin1(byte[] c)
	{
		if	(c.length>0)	System.arraycopy(c, 0, pin1, 0, (c.length>pin1.length?pin1.length:c.length));
	}

	public byte[] getPin2()
	{
		return this.pin2;
	}
	public void setPin2(byte[] c)
	{
		if	(c.length>0)	System.arraycopy(c, 0, pin2, 0, (c.length>pin2.length?pin2.length:c.length));
	}

	public byte[] getPin3()
	{
		return this.pin3;
	}
	public void setPin3(byte[] c)
	{
		if	(c.length>0)	System.arraycopy(c, 0, pin3, 0, (c.length>pin3.length?pin3.length:c.length));
	}

	public byte[] getPin4()
	{
		return this.pin4;
	}
	public void setPin4(byte[] c)
	{
		if	(c.length>0)	System.arraycopy(c, 0, pin4, 0, (c.length>pin4.length?pin4.length:c.length));
	}


	public byte[] getNewpin()
	{
		return this.newpin;
	}
	public void setNewpin(byte[] c)
	{
		if	(c.length>0)	System.arraycopy(c, 0, newpin, 0, (c.length>newpin.length?newpin.length:c.length));
	}



}

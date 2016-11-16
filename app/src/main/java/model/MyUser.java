package model;

import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	
	private static final long serialVersionUID = 1L;
	private  String yzm;
	private  String NIckName;
	private  String ImageHead;
	private  boolean IsPay;
	private String ImgHeadUrl;
	private  boolean IsLogined;
	public boolean isIsPay() {
		return IsPay;
	}
	private  String PayDate;
	private  int id;
	private List<String> goodAt;
	private List<JSONObject> notice;


	public List<String> getGoodAt() {
		return goodAt;
	}

	public void setGoodAt(List<String> goodAt) {
		this.goodAt = goodAt;
	}

	public List<JSONObject> getNotice() {
		return notice;
	}

	public void setNotice(List<JSONObject> notice) {
		this.notice = notice;
	}
	public String getYzm() {
		return yzm;
	}

	public void setYzm(String l) {
		this.yzm = l;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isPay() {
		return IsPay;
	}

	public void setIsPay(boolean isPay) {
		IsPay = isPay;
	}

	public String getPayDate() {
		return PayDate;
	}

	public void setPayDate(String payDate) {
		PayDate = payDate;
	}

	@Override
	public String toString() {
		return "MyUser [yzm=" + yzm + ", IsPay=" + IsPay + ", PayDate=" + PayDate + ", id=" + id + ", goodAt=" + goodAt
				+ ", notice=" + notice + "]";
	}

	public boolean isIsLogined() {
		return IsLogined;
	}

	public void setIsLogined(boolean isLogined) {
		IsLogined = isLogined;
	}

	public String getImageHead() {
		return ImageHead;
	}

	public void setImageHead(String imageHead) {
		ImageHead = imageHead;
	}

	public String getImgHeadUrl() {
		return ImgHeadUrl;
	}

	public void setImgHeadUrl(String imgHeadUrl) {
		ImgHeadUrl = imgHeadUrl;
	}

	public String getNIckName() {
		return NIckName;
	}

	public void setNIckName(String NIckName) {
		this.NIckName = NIckName;
	}
}

package com;

/**
 * 课程信息
 * @author hjg
 *
 */
public class Course {
	/**
	 * 序号
	 */
	private int id;
	/**
	 * 学年
	 */
	private String year;
	/**
	 * 学期
	 */
	private String term;
	/**
	 * 课程名
	 */
	private String name;
	/**
	 * 学分
	 */
	private String credit;
	/**
	 * 性质
	 */
	private String nature;
	/**
	 * 成绩
	 */
	private String grade;
	public int getId() {
		return id;
	}
	public String getYear() {
		return year;
	}
	public String getTerm() {
		return term;
	}
	public String getName() {
		return name;
	}
	public String getCredit() {
		return credit;
	}
	public String getNature() {
		return nature;
	}
	public String getGrade() {
		return grade;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	@Override
	public String toString() {
		return "id:"+id+";year:"+year+term+";name:"+name+
				";credit:"+credit+";nature:"+nature+";grade:"+grade;
	}
}

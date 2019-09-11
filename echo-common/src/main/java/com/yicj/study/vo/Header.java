package com.yicj.study.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Header {
	private byte tag;
	/* 编码 */
	private byte encode;
	/* 加密 */
	private byte encrypt;
	/* 其他字段 */
	private byte extend1;
	/* 其他2 */
	private byte extend2;
	/* 会话id */
	private String sessionid;
	/* 包的长度 */
	private int length = 1024;
	/* 命令 */
	private int cammand;

	public Header(String sessionid) {
		this.encode = 0;
		this.encrypt = 0;
		this.sessionid = sessionid;
	}
}

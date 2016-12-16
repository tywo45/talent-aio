/**
 * 
 */
package com.talent.aio.examples.im.common.bs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@lombok.AllArgsConstructor
public class EndshowRespBody extends BaseRespBody
{
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(EndshowRespBody.class);

	/**
	 * 
	 */
	public EndshowRespBody()
	{

	}

	private Integer liveshowid;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

}

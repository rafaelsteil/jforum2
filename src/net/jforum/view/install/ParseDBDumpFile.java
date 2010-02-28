/*
 * Copyright (c) JForum Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * Created on 16/11/2005 18:50:18
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.install;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Steil
 * @version $Id: ParseDBDumpFile.java,v 1.6 2007/10/08 17:34:40 rafaelsteil Exp $
 */
public class ParseDBDumpFile
{
	public static List parse(String filename) throws IOException
	{
		List statements = new ArrayList();
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				line = line.trim();
			
				if (line.length() == 0) {
					continue;
				}
				
				char firstChar = line.charAt(0);
				
				if (firstChar == '-' || firstChar == '#') {
					continue;
				}
				
				if (line.charAt(line.length() - 1) == ';') {
					line = line.substring(0, line.length() - 1);
				}
				
				statements.add(line);
			}
		}
		finally {
			if (reader != null) {
				try { reader.close(); } catch (Exception e) {}
			}
		}
		
		return statements;
	}
}

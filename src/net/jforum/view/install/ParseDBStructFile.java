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
 * Created on 16/11/2005 18:42:42
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.install;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import net.jforum.exceptions.ForumException;

/**
 * @author Rafael Steil
 * @version $Id: ParseDBStructFile.java,v 1.7 2007/07/28 14:17:13 rafaelsteil Exp $
 */
public class ParseDBStructFile
{
    public static List parse(String filename)
	{
		List statements = new ArrayList();
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			StringBuffer sb = new StringBuffer(512);

			boolean processing = false;
			char delimiter = ';';
			String[] creators = { "CREATE INDEX", "CREATE TABLE", "CREATE SEQUENCE", "DROP TABLE", "IF EXISTS",
					"DROP SEQUENCE", "DROP INDEX" };
			
            String line ;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				
				char charAt = line.charAt(0);
				
				// Ignore comments
				if (charAt == '-' || charAt == '#') {
					continue;
				}
				
				if (processing) {
					sb.append(line);
					
					if (line.indexOf(delimiter) > -1) {
						sb.delete(sb.length() - 1, sb.length());
						statements.add(sb.toString());
						processing = false;
					}
				}
				else {
					for (int i = 0; i < creators.length; i++) {
						if (line.indexOf(creators[i]) > -1) {
							sb.delete(0, sb.length());
							
							if (line.indexOf(delimiter) > -1) {
								if (line.indexOf(';') > -1) {
									line = line.replace(';', ' ');
								}
								
								statements.add(line);
							}
							else {
								sb.append(line);
								processing = true;
							}
							
							break;
						}
					}
				}
			}
		}
        catch (Exception e)
        {
            throw new ForumException(e);
        }
        finally {
			if (reader != null) {
				try { reader.close(); }
                catch (Exception e) {
                    // catch close BufferedReader
                }
			}
		}
		
		return statements;
	}
}

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
 * Created on Apr 2, 2005 / 9:15:13 AM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum.common;

import java.util.List;
import java.util.Random;

import net.jforum.dao.BannerDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Banner;

/**
 * @author Samuel Yung
 * @version $Id: BannerCommon.java,v 1.9 2007/08/01 22:30:05 rafaelsteil Exp $
 */
public class BannerCommon
{
	private BannerDAO dao;
	private List banners;

	public BannerCommon()
	{
		this.dao = DataAccessDriver.getInstance().newBannerDAO();
	}

    /**
     * Check whether the banner will be displayed based on user rights and
     * banner filter settings.
     * @return boolean
     * @param bannerId int
     */
    public boolean canBannerDisplay(int bannerId)
	{
    	return true;
	}

	/**
	 * Test whether any active banner exist at the placement indicated.
	 * @param placement int
	 * @return boolean
	 */
	public boolean isActiveBannerExist(int placement) 
	{
		banners = dao.selectActiveBannerByPlacement(placement);
		if (banners == null || banners.isEmpty())
		{
			return false;
		}
		
		return true;
	}

	/**
	 * Retrieves the correct banner based on weight. Before calling this
	 * function the isBannerExist(int placement) must be called. The total
	 * weight for all the same position banners should be equal to 99. If
	 * the total weight is smaller than 99 and the random number is larger
	 * than the total weight of all the same position banners, the highest
	 * weight's banner will be chosen. After a correct banner is found, its
	 * views variable will be incremented by 1.
	 *
	 * @return Banner
	 */
	public Banner getBanner()
	{
		Banner result = null;

		if (banners == null || banners.isEmpty())
		{
			return null;
		}

		// get correct banner based on weight
		int r = (new Random().nextInt(99));
		int weightFrom = 0;
		int weightTo = 0;
		for(int i = 0; i < banners.size(); i++)
		{
			result = (Banner)banners.get(i);
			weightTo += result.getWeight();
			if (r >= weightFrom && r < weightTo)
			{
				break;
			}
			weightFrom = weightTo;
		}

        // increment views by 1
		result.setViews(result.getViews() + 1);
		dao.update(result);

		return result;
	}
}

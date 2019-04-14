package com;

import controller.Controller;
import javafx.stage.Stage;
import model.database.Database;

public class Test
{
	
	
	public static void main(String [] args)
	{
		try
		{
			Database database = new Database(new Controller(new Stage()));
			System.out.println(database.isAdURLPresent("https://www.zillow.com/homes/make_me_move/Orlando-FL/mmm_pt/13121_rid/2-_beds/123899-958970_price/496-3839_mp/globalrelevanceex_sort/28.824823,-80.768738,28.134366,-81.706696_rect/9_zm/", "https://www.zillow.com/homedetails/43-Bentford-Ct-Orlando-FL-32817/46101331_zpid/"));
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
		

	}	
}
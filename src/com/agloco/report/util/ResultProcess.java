package com.agloco.report.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Erick Kong
 * 
 */
public class ResultProcess implements SuffixScriptInterface
{

	public AGReportResultList processResult(List lists)
	{
		DecimalFormat df = new DecimalFormat("0.00%");
		
		
		
		// TODO Auto-generated method stub
		AGReportResultList rl = new AGReportResultList();
		List tmpList1 = (List)lists.get(0);
		List tmpList2 = (List)lists.get(1);
		List result = new ArrayList();
		for(int idx=0;idx<tmpList1.size();idx++)
		{
			Object[] tmp = new Object[]{new Object(),new Double(0),new Double(0),new Double(0)};
			Object[] tmpData = (Object[])tmpList1.get(idx);
			tmp[0] = tmpData[0];
			tmp[1] = tmpData[1];
			
			for(int idx2=0;idx2<tmpList2.size();idx2++)
			{
				Object[] tmpData2 = (Object[])tmpList2.get(idx2);
				if(tmpData2[0].equals(tmpData[0]))
				{
					tmp[2] = tmpData2[1];

					tmpList2.remove(idx2);
					break;
				}
			}
			if(Double.parseDouble(tmp[2].toString()) > 0)
				tmp[3] = new Double((Double.parseDouble(tmp[1].toString())-Double.parseDouble(tmp[2].toString()))
					/Double.parseDouble(tmp[2].toString()));
			else
				tmp[3] = new Double(Double.NaN);
			result.add(tmp);
		}
		for(int idx=0;idx<tmpList2.size();idx++)
		{
			Object[] tmp = new Object[]{new Object(),new Double(0),new Double(0),new Double(0)};
			Object[] tmpData = (Object[])tmpList1.get(idx);
			tmp[0] = tmpData[0];
			tmp[1] = new Double(0);
			tmp[2] = tmpData[1];
			tmp[3] = df.format((Double.parseDouble(tmp[1].toString())-Double.parseDouble(tmp[2].toString()))
			/Double.parseDouble(tmp[2].toString()));
			result.add(tmp);
		}
		rl.setList(result);

		return rl;
	}
}

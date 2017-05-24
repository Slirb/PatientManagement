/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author vcurtis
 */
public class Labs 
{
    private int LabID;
    private String TestType;
    private String TestDescription;
    private String TestResults;
    private GregorianCalendar TestDateTime;
    
    
    //Constructors    
    public Labs(int ID, String Type, String Desc, String Results, GregorianCalendar Date)
    {
        LabID=ID;
        TestType=Type;
        TestDescription=Desc;
        TestResults=Results;
        TestDateTime= new GregorianCalendar(Date.get(Date.YEAR), Date.get(Date.MONTH),Date.get(Date.DAY_OF_MONTH));
   }
    
    
    //Set Data
    public void SetID(int ID)
    {
        LabID=ID;
    }
    
    public void SetType(String Type)
    {
        TestType=Type;
    }
     
    public void SetDescription(String Desc)
    {
        TestDescription=Desc;
    }  
       
    public void SetResults(String Results)
    {
        TestResults=Results;
    } 
     
    public void SetDate(GregorianCalendar Date)
    {
        TestDateTime= new GregorianCalendar(Date.get(Date.YEAR), Date.get(Date.MONTH),Date.get(Date.DAY_OF_MONTH));
    }  

    
     //Set Data
    public int GetID()
    {
        return LabID;
    }
    
    public String GetType()
    {
        return TestType;
    }
     
    public String GetDescription()
    {
        return TestDescription;
    }  
       
    public String GetResults()
    {
        return TestResults;
    } 
     
    public String GetDate()
    {
       //Formats encounter date into yyyy-mm-dd format
        Date greg=TestDateTime.getTime();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(greg);
        return Date;
    }  
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject;

import java.text.*;
import java.util.*;
/**
 *
 * @author vcurtis
 */
public class VitalSigns 
{
    private int VitalSignID;
    private String Height;
    private double Weight;
    private String Heart;
    private String BloodPressure;
    private double Temperature;
    private GregorianCalendar VitalDateTime;
    
    
    
    //Constructors    
    public VitalSigns(int ID, String Height, double Weight,String Heart, String Pressure, double Temp, GregorianCalendar Date)
    {
        VitalSignID=ID;
        this.Height=Height;
        this.Weight=Weight;
        this.Heart=Heart;
        BloodPressure=Pressure;
        Temperature=Temp;
        VitalDateTime= new GregorianCalendar(Date.get(Date.YEAR), Date.get(Date.MONTH),Date.get(Date.DAY_OF_MONTH));
   }
    
    
    //Set Data
    public void SetID(int ID)
    {
        VitalSignID=ID;
    }
    
    public void SetHeight(String Height)
    {
        this.Height=Height;
    }
     
    public void SetWeight(double Weight)
    {
        this.Weight=Weight;
    }
    
    public void SetHeart(String Heart)
    {
        this.Heart=Heart;
    } 
       
    public void SetBloodPressure(String Pressure)
    {
        BloodPressure=Pressure;
    } 
    
    public void SetTemperature(double Temp)
    {
        this.Temperature=Temp;
    }
     
    public void SetVitalDateTime(GregorianCalendar Date)
    {
        VitalDateTime= new GregorianCalendar(Date.get(Date.YEAR), Date.get(Date.MONTH),Date.get(Date.DAY_OF_MONTH));
    }
    
    
    //Get Data
    public int GetID()
    {
        return VitalSignID;
    }
    
    public String GetHeight()
    {
        return Height;
    }
     
    public double GetWeight()
    {
        return Weight;
    } 
    
    public String GetHeart()
    {
        return Heart;
    }
       
    public String GetBloodPressure()
    {
        return BloodPressure;
    } 
    
    public double GetTemperature()
    {
        return Temperature;
    }
     
    public String GetVitalDateTime()
    {
        //Formats encounter date into yyy-mm-dd format
        
        Date greg=VitalDateTime.getTime();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(greg);
        return Date;
    }
}

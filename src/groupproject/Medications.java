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
public class Medications 
{
    private int MedicationID;
    private String MedName;
    private double Dosage;
    private String DosageUnits;
    private String DosageFrequency;
    private GregorianCalendar StartDate;
    private GregorianCalendar EndDate;
    private String Instructions;
    private String PrescribedBy;
    
 
    //Constructors    
    public Medications(int ID, String Name, double Dosage, String Units, String Frequency, GregorianCalendar SDate,
            GregorianCalendar EDate, String Inst, String Presc)
    {
        MedicationID=ID;
        MedName=Name;
        this.Dosage=Dosage;
        DosageUnits=Units;
        DosageFrequency=Frequency;
        StartDate=new GregorianCalendar(SDate.get(SDate.YEAR), SDate.get(SDate.MONTH),SDate.get(SDate.DAY_OF_MONTH));
        EndDate= new GregorianCalendar(EDate.get(EDate.YEAR), EDate.get(EDate.MONTH),EDate.get(EDate.DAY_OF_MONTH));
        Instructions=Inst;
        PrescribedBy=Presc;
    }
    
    public Medications(int ID, String Name, double Dosage, String Units, String Frequency, GregorianCalendar SDate,
             String Inst, String Presc)
    {
        MedicationID=ID;
        MedName=Name;
        this.Dosage=Dosage;
        DosageUnits=Units;
        DosageFrequency=Frequency;
        StartDate=new GregorianCalendar(SDate.get(SDate.YEAR), SDate.get(SDate.MONTH),SDate.get(SDate.DAY_OF_MONTH));
        EndDate=null;
        Instructions=Inst;
        PrescribedBy=Presc;
    }
    
    //Set Data
    public void SetID(int ID)
    {
        MedicationID=ID;
    }
    
    public void SetName(String Name)
    {
        MedName=Name;
    }
    
    public void SetDosage(double Dosage)
    {
        this.Dosage=Dosage;
    }
    
    public void SetDosageUnits(String Units)
    {
        DosageUnits=Units;
    }
    
    public void SetDosageFrequency(String Frequency)
    {
        DosageFrequency=Frequency;
    }
    
    public void SetStartDate(GregorianCalendar SDate)
    {
        StartDate=new GregorianCalendar(SDate.get(SDate.YEAR), SDate.get(SDate.MONTH),SDate.get(SDate.DAY_OF_MONTH));  
    }
    
    public void SetEndDate(GregorianCalendar EDate)
    {
       EndDate= new GregorianCalendar(EDate.get(EDate.YEAR), EDate.get(EDate.MONTH),EDate.get(EDate.DAY_OF_MONTH)); 
    }
    
    public void SetInstructions(String Inst)
    {
        Instructions=Inst;
    }
    
    public void SetPrescribedBy(String Presc)
    {
     PrescribedBy=Presc;   
    }
    
    
    //Get Data
    public int GetID()
    {
        return MedicationID;
    }
    
    public String GetName()
    {
        return MedName;
    }
    
    public double GetDosage()
    {
        return Dosage;
    }
    
    public String GetDosageUnits()
    {
        return DosageUnits;
    }
    
    public String GetDosageFrequency()
    {
        return DosageFrequency;
    }
    
    public String GetStartDate()
    {
        //Formats encounter date into yyyy-mm-dd format
        Date greg=StartDate.getTime();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(greg);
        return Date;  
    }
    
    public String GetEndDate()
    {       
       //Formats encounter date into yyyy-mm-dd format
        Date greg=EndDate.getTime();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(greg);
        if(EndDate==null)
        {
          Date="";  
        }
        return Date; 
    }
    
    public String GetInstructions()
    {
        return Instructions;
    }
    
    public String GetPrescribedBy()
    {
     return PrescribedBy;   
    }
}

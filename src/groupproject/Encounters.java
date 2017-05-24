/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author vcurtis
 */
public class Encounters 
{
    private int EncounterID;
    private GregorianCalendar EncounterDate;
    private String EncounterType;
    private String Notes;
    private String DiagnosisDescription;
    private String Provider;
    
    
//Constructors    
    public Encounters(int ID, GregorianCalendar Date, String Type, String Note, String Desc, String Prov)
    {
        EncounterID=ID;
        EncounterDate= new GregorianCalendar(Date.get(Date.YEAR), Date.get(Date.MONTH),Date.get(Date.DAY_OF_MONTH));
        EncounterType=Type;
        Notes=Note;
        DiagnosisDescription=Desc;
        Provider=Prov;
    }
    
    //Set Data
    public void SetID(int ID)
    {
        EncounterID=ID;
    }
    
    public void SetDate(GregorianCalendar Date)
    {
        EncounterDate= new GregorianCalendar(Date.get(Date.YEAR), Date.get(Date.MONTH),Date.get(Date.DAY_OF_MONTH));
    }
    
    public void SetType(String Type)
    {
        EncounterType=Type;
    }
    
    public void SetNotes(String Note)
    {
        Notes=Note;
    }
    
    public void SetDescription(String Desc)
    {
        DiagnosisDescription=Desc;
    }
    
    public void SetProvider(String Prov)
    {
        Provider=Prov;
    }
    
    //Get Data
    public int GetID()
    {
        return EncounterID;
    }
    
    public String GetDate()
    {
        //Formats encounter date into yyyy-mm-dd format
        Date greg=EncounterDate.getTime();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(greg);
        return Date;
    }
    
    public String GetType()
    {
        return EncounterType;
    }
    
    public String GetNotes()
    {
        return Notes;
    }
    
    public String GetDescription()
    {
        return DiagnosisDescription;
    }
    
    public String GetProvider()
    {
        return Provider;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject;


import java.util.*;
import java.text.*;

/**
 *
 * @author vcurtis
 */
public class Patient 
{
    //Unique for each patient
    private int PatientID;
    private String RecordNumber;
    private String AltRecordNumber;
    private String Sex;
    private String LastName;
    private String FirstName;
    private String Middle;
    private String Address;
    private String City;
    private String State;
    private String ZipCode;
    private GregorianCalendar BirthDate;
    private int Age;
    private String HomePhone;
    private String WorkPhone;
    private String Email;
    private String NextOfKin;
    private String NextOfKinContact;
    private String GeneralNotes;
    
    
    //Constructors
    public Patient(int ID, String Num, String AltNum, String Sex, String Last, String First, String Mid, String Addy, String City, String State, String Zip, GregorianCalendar Date, String Home, String Work, String Email,
                String Kin, String KinContact, String Notes)
    {
        PatientID=ID;
        RecordNumber=Num;
        AltRecordNumber=AltNum;
        this.Sex=Sex;
        LastName=Last;
        FirstName=First;
        Middle=Mid;
        Address=Addy;
        this.City=City;
        this.State=State;
        ZipCode=Zip;
        BirthDate= new GregorianCalendar(Date.get(Date.YEAR), Date.get(Date.MONTH),Date.get(Date.DAY_OF_MONTH));
        
        //Calculate age
        GregorianCalendar CurDate=new GregorianCalendar();
        int diff=0;
        if(CurDate.get(Calendar.DAY_OF_YEAR) < BirthDate.get(Calendar.DAY_OF_YEAR)) 
        {
        diff = -1; 
        }
        Age = CurDate.get(Calendar.YEAR) - BirthDate.get(Calendar.YEAR) + diff;
        
        HomePhone=Home;
        WorkPhone=Work;
        this.Email=Email;
        NextOfKin=Kin;
        NextOfKinContact=KinContact;
        GeneralNotes=Notes;
    }
    
    //GetData
    public int GetID()
    {
        return PatientID;
    }
    
    public String GetRecord()
    {
        return RecordNumber;
    }
    
    public String GetAltRecord()
    {
        return AltRecordNumber;
    }
    
    public String GetSex()
    {
        return Sex;
    }
    
    public String GetLast()
    {
        return LastName;
    }
    
    public String GetFirst()
    {
        return FirstName;
    }
    public String GetMiddle()
    {
        return Middle;
    }
    
    public String GetAddress()
    {
        return Address;
    }

    public String GetCity()
    {
        return City;
    }
    
    public String GetState()
    {
        return State;
    }
    
    public String GetZipCode()
    {
        return ZipCode;
    }
    
    public String GetBirthDate()
    {
        //Formats Birth Date into yyy-mm-dd format
        
        Date greg=BirthDate.getTime();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(greg);
        return Date;
    }
    
    public int GetAge()
    {
        return Age;
    }
    
    public String GetHomePhone()
    {
        return HomePhone;
    }
    
    public String GetWorkPhone()
    {
        return WorkPhone;
    }
    
    public String GetEmail()
    {
        return Email;
    }

    public String GetNextOfKin()
    {
        return NextOfKin;
    }
    
    public String GetNextOfKinContact()
    {
        return NextOfKinContact;
    }
    
    public String GetNotes()
    {
        return GeneralNotes;
    }
}
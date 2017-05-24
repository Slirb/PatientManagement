/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.nio.file.*;
import java.io.*;

/**
 *
 * @author Team5CNIT350
 */
public class PatientDemographics extends javax.swing.JFrame 
{
    
    
    //Global Variables    
    Patient Incoming;                           //Stores the currently selected patient    
    int CurrentPatient=0;                       //The current patient id.  Used for navigation
    int MaxPatientRecords=0;                    //Max number of patients in the database.  Used for navigation
    
    ArrayList<Encounters> PatientEncounter;     //Stores the list of encounters for the selected patient
    int CurrentEncounter=0;                     //Current index of the selected encounter in the array list
    int MaxEncounters=0;                        //Max number of encounters for a patient.  Used for navigation
    
    ArrayList<VitalSigns> PatientVital;         //Stores the list of vitals for the selected patient
    int CurrentVital=0;                         //Current index of the selected vital in the array list
    int MaxVital=0;                             //Max number of vitals for a patient.  Used for navigation
    
    ArrayList<Medications> PatientMedication;   //Stores the list of medications for the selected patient
    int CurrentMedication=0;                    //Current index of the selected medication in the array list
    int MaxMedication=0;                        //Max number of medications for a patient.  Used for navigation
    
    ArrayList<Labs> PatientLab;                 //Stores the list of labs/procedures for the selected patient
    int CurrentLab=0;                           //Current index of the selected lab/procedure in the array list
    int MaxLab=0;                               //Max number of labs/procedures for a patient.  Used for navigation
    
    
    /**
     * Creates new form PatientDemographics
     */
    public PatientDemographics() 
    {
        initComponents();
        
        GlobalNameLabel.setText(null);
        GlobalAgeLabel.setText(null);
        GlobalSexLabel.setText(null);
        getPatient(CurrentPatient);
        DisplayPatient();
             
        
        CancelAddButton.setVisible(false);
        SaveModifyButton.setVisible(false);
    }
    //Runs SQL statement on the database to get a patient
    private void getPatient(int Number)
    {  
       String sql="SELECT * FROM PATIENTS LIMIT "+
                        Number+",1";
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(sql);)
        {    
            
            while(rs.next())
                {
                int ID=rs.getInt("Patient_ID");
                String num=rs.getString("Medical_Number");
                String alt=rs.getString("Alt_Record_Number");
                String Sex=rs.getString("Sex");
                String Last=rs.getString("Last_Name");
                String First=rs.getString("First_Name");
                String Mid=rs.getString("Middle");
                String Addy=rs.getString("Address");
                String City=rs.getString("City");
                String State=rs.getString("State");
                String Zip=rs.getString("Zip_Code");
                //Get date from datbase and insert into object
                GregorianCalendar Date=new GregorianCalendar();
                Date.setTime(rs.getTimestamp("Birth_Date"));
                
                String HomePhone=rs.getString("Home_Phone");
                String WorkPhone=rs.getString("Work_Phone");
                String Email=rs.getString("Email");
                String Kin=rs.getString("Next_Of_Kin");
                String KinContact=rs.getString("Next_Of_Kin_Contact");
                String Notes=rs.getString("General_Notes");
                
                //Stores the current patient in an object to be accessed by the form
                Incoming= new Patient(ID, num, alt, Sex, Last, First, Mid, Addy, City, State, Zip, Date, HomePhone, WorkPhone, Email, Kin, KinContact, Notes);
                
                }
            
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Sets the max records number to the count of all records in the database
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SELECT Count(*) FROM PATIENTS");)
        {    
            while(rs.next())
            {
            
                MaxPatientRecords=rs.getInt("Count(*)");
                MaxPatientRecords--;
                
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        
        
    }
    //Creates the connection for the database
    private Connection getConnection()
    {
        Connection connection=null;
        try
        {
            String dbUrl = "jdbc:mysql://localhost:3306/patients";
            String username ="root";
            String password="mysqlroot";
            connection=DriverManager.getConnection(dbUrl, username, password);
            return connection;
        }
        catch(SQLException e)
        {
           for (Throwable t : e)
               e.printStackTrace();
           return null;
        }
    }
    //Displays a given record from the Array List
    private void DisplayPatient()
    {
    Patient t=Incoming;
    
    //Set Global labels
    GlobalNameLabel.setText(t.GetLast()+", "+t.GetFirst());
    GlobalAgeLabel.setText(String.valueOf(t.GetAge()));
    GlobalSexLabel.setText(t.GetSex());
    
    //Set form text boxes
    PatientIDBox.setText(String.valueOf(t.GetID()));
    RecordNumBox.setText(t.GetRecord());
    AltRecordNumBox.setText(t.GetAltRecord());
    LastNameBox.setText(t.GetLast());
    FirstNameBox.setText(t.GetFirst());
    MiddleInitBox.setText(t.GetMiddle());
    AddressBox.setText(t.GetAddress());
    CityBox.setText(t.GetCity());
    StateBox.setText(t.GetState());
    ZipBox.setText(t.GetZipCode());
    BirthDayBox.setText(t.GetBirthDate());
    AgeBox.setText(String.valueOf(t.GetAge()));
    HomePhoneBox.setText(t.GetHomePhone());
    WorkPhoneBox.setText(t.GetWorkPhone());
    EmailBox.setText(t.GetEmail());
    NextOfKinBox.setText(t.GetNextOfKin());
    KinContactBox.setText(t.GetNextOfKinContact());
    NotesBox.setText(t.GetNotes());
    SexBox.setText(t.GetSex());
    }
    
    //Displays the current encounter on the encounter tab
    private void DisplayEncounter(int incoming)
    {
    
    Encounters t=PatientEncounter.get(incoming);
        
    
    EncounterIDBox.setText(String.valueOf(t.GetID()));
    EncounterDateBox.setText(t.GetDate());
    EncounterTypeBox.setSelectedItem(t.GetType());
    EncounterNotesBox.setText(t.GetNotes());
    EncounterDiagnosisBox.setText(t.GetDescription());
    EncounterProviderBox.setText(t.GetProvider());
    
    
 
    }
    
    
    //Displays the current vitals on the labs tab
    private void DisplayVital(int incoming)
    {
    
    VitalSigns t=PatientVital.get(incoming);
        
    
    VitalIDBox.setText(String.valueOf(t.GetID()));
    
    //Splits Height into both text boxes
    String[] height = t.GetHeight().split("-");
    String ft = height[0]; 
    String in = height[1];
    VitalFootBox.setText(ft);
    VitalInchBox.setText(in);
    
    
    VitalWeightBox.setText(String.valueOf(t.GetWeight()));
    
    VitalHRBox.setText(t.GetHeart());
    //Splits Blood pressure into both text boxes
    String[] BP= t.GetBloodPressure().split("/");
    String sys = BP[0]; 
    String dia = BP[1];
    VitalSystolicBox.setText(sys);
    VitalDiastolicBox.setText(dia);
    
    
    VitalTempBox.setText(String.valueOf(t.GetTemperature()));
    VitalDateBox.setText(t.GetVitalDateTime());
    
    
    VitalListBox.removeAll();
    //Selects the previous vitals into an array list
    int tempid=Incoming.GetID();
    String CurrentMed="SELECT * FROM Vitalsigns WHERE Patient_ID="+tempid;
    ArrayList<String> preVital=new ArrayList<>();
        
    
        
    try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(CurrentMed);)
        {    
            while(rs.next())
            {
                //Converts MYSQL Date into Gregorian Calendar format
                GregorianCalendar Date=new GregorianCalendar();
                Date.setTime(rs.getTimestamp("Vital_Date_Time"));
                Date one=Date.getTime();
                String day = new SimpleDateFormat("MMM dd, yyyy").format(one);
                preVital.add(day+"     Height:"+rs.getString("Height")+"  Weight:"+rs.getString("Weight")+"  Heart Rate:"+rs.getString("HeartRate")+"    BP:"+rs.getString("BloodPressure")+"  Temp:"+rs.getString("Temperature"));
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
    //Converts the arraylist into an array and displays it in the listbox
    VitalListBox.setListData(preVital.toArray());
    }
    
    //Displays the current medication on the medicationss tab
    private void DisplayMedication(int incoming)
    {
    Medications t=PatientMedication.get(incoming);
        
    MedicationIDBox.setText(String.valueOf(t.GetID()));
    MedicationNameBox.setText(t.GetName());
    DosageBox.setText(Double.toString(t.GetDosage()));
    DosageUnitsComboBox.setSelectedItem(t.GetDosageUnits());
    DosageFrequencyComboBox.setSelectedItem(t.GetDosageFrequency());
    MedicationStartDateBox.setText(t.GetStartDate());
    MedicationEndDateBox.setText(t.GetEndDate());
    InstructionsBox.setText(t.GetInstructions());
    PrescribedByBox.setText(t.GetPrescribedBy());
    CurrentMedicationButton.setVisible(false);
    UnitErrorLabel.setVisible(false);
    FreqErrorLabel.setVisible(false);
    CurrentMedicationButton.doClick();
    }
    
    //Displays the current lab on the labs tab
    private void DisplayLab(int incoming)
    {
    Labs t=PatientLab.get(incoming);
        
    LabIDBox.setText(String.valueOf(t.GetID()));
    LabTestTypeComboBox.setSelectedItem(t.GetType());
    LabTestDescriptionBox.setText(t.GetDescription());
    LabTestResultsBox.setText(t.GetResults());
    LabTestDateTimeBox.setText(t.GetDate());
    TypeErrorLabel.setVisible(false);
    AllLabsProceduresButton.doClick();
    }
    
    //Inserts labs into an array for the current patient
    private void getLab()
    {
        
        //Creates a new blank array list for the labs
        PatientLab=new ArrayList<>();
        
        //Gets patient id for use in the sql statement
        int tempid=Incoming.GetID();
        
        String SQL="SELECT * FROM Labs WHERE Patient_ID="+tempid+
                " ORDER BY Test_Date_Time DESC";
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(SQL);)
        {    
            
            //Loops through the records and inserts them into an Array List
            while(rs.next())
            {
                int ID=rs.getInt("Lab_ID");
                //Converts MYSQL Date into Gregorian Calendar format
                GregorianCalendar Date=new GregorianCalendar();
                Date.setTime(rs.getTimestamp("Test_Date_Time"));
                
                String Type=rs.getString("Test_Type");
                String Desc=rs.getString("Test_Description");
                String Results=rs.getString("Test_Results");
                
                
                Labs p= new Labs(ID, Type, Desc, Results, Date);
                PatientLab.add(p);                 
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Sets the max records number to the count of all records in the database
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SELECT Count(*) FROM Labs WHERE Patient_ID="+tempid);)
        {    
            while(rs.next())
            {
            
                MaxLab=rs.getInt("Count(*)"); 
                MaxLab--;
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
    }
    
//Inserts vital signs into an array for the current patient
    private void getVitals()
    {
        
        //Creates a new blank array list for the labs
        PatientVital=new ArrayList<>();
        
        //Gets patient id for use in the sql statement
        int tempid=Incoming.GetID();
        
        String SQL="SELECT * FROM Vitalsigns WHERE Patient_ID="+tempid+
                " ORDER BY Vital_Date_Time DESC";
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(SQL);)
        {    
            
            //Loops through the records and inserts them into an Array List
            while(rs.next())
            {
                int ID=rs.getInt("Vital_Sign_ID");
                //Converts MYSQL Date into Gregorian Calendar format
                GregorianCalendar Date=new GregorianCalendar();
                Date.setTime(rs.getTimestamp("Vital_Date_Time"));
                
                String Height=rs.getString("Height");
                Double Weight=rs.getDouble("Weight");
                String Heart=rs.getString("HeartRate");
                     
                String BP=rs.getString("BloodPressure");
                Double Temp=rs.getDouble("Temperature");
                
                
                VitalSigns p= new VitalSigns(ID, Height, Weight, Heart, BP, Temp, Date);
                PatientVital.add(p);                 
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Sets the max records number to the count of all records in the database
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SELECT Count(*) FROM Vitalsigns WHERE Patient_ID="+tempid);)
        {    
            while(rs.next())
            {
            
                MaxVital=rs.getInt("Count(*)"); 
                MaxVital--;
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
    }
    
    
    
    
    //Inserts a medication list into an array for the current patient
    private void getMedication()
    {
        
        //Creates a new blank array list for the medications
        PatientMedication=new ArrayList<>();
        
        //Gets patient id for use in the sql statement
        int tempid=Incoming.GetID();
        
        String SQL="SELECT * FROM Medications WHERE Patient_ID="+tempid+" ORDER BY End_Date ";
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(SQL);)
        {    
           
            //Loops through the records and inserts them into an Array List
            while(rs.next())
            {
                int ID=rs.getInt("Medication_ID");
                //Converts MYSQL Date into Gregorian Calendar format
                GregorianCalendar SDate=new GregorianCalendar();
                SDate.setTime(rs.getTimestamp("Start_Date"));
                GregorianCalendar EDate=new GregorianCalendar();
                
               
                String Name=rs.getString("Med_Name");
                Double Dosage=rs.getDouble("Dosage");
                String Units=rs.getString("Dosage_Units");
                String Frequency=rs.getString("Dosage_Frequency");
                String Inst=rs.getString("Instructions");
                String Presc=rs.getString("Prescribed_By");
                
                if(rs.getTimestamp("End_Date")!=null)
                {
                EDate.setTime(rs.getTimestamp("End_Date"));
                Medications p= new Medications(ID, Name, Dosage, Units, Frequency, SDate, EDate, Inst, Presc);
                PatientMedication.add(p);                 
                }
                else
                {
                Medications p= new Medications(ID, Name, Dosage, Units, Frequency, SDate, Inst, Presc);
                PatientMedication.add(p); 
                }
                
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Sets the max records number to the count of all records in the database
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SELECT Count(*) FROM Medications WHERE Patient_ID="+tempid);)
        {    
            while(rs.next())
            {            
                MaxMedication=rs.getInt("Count(*)"); 
                MaxMedication--;
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PatientDemographics = new javax.swing.JTabbedPane();
        PatientsDemoPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        CityBox = new javax.swing.JTextField();
        PreviousButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        NextButton = new javax.swing.JButton();
        StateBox = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        ZipBox = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        BirthDayBox = new javax.swing.JTextField();
        PatientIDBox = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        CreateRecordButton = new javax.swing.JButton();
        AgeBox = new javax.swing.JTextField();
        RecordNumBox = new javax.swing.JTextField();
        AltRecordNumBox = new javax.swing.JTextField();
        HomePhoneBox = new javax.swing.JTextField();
        LastNameBox = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        FirstNameBox = new javax.swing.JTextField();
        WorkPhoneBox = new javax.swing.JTextField();
        MiddleInitBox = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        AddressBox = new javax.swing.JTextField();
        EmailBox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        NextOfKinBox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        KinContactBox = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        DemoAddRecordButton = new javax.swing.JButton();
        SexBox = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        ModifyDemoButton = new javax.swing.JButton();
        CancelModifyDemoButton = new javax.swing.JButton();
        CancelAddButton = new javax.swing.JButton();
        SaveModifyButton = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        NotesBox = new javax.swing.JTextArea();
        DemoDelete = new javax.swing.JButton();
        EncountersPanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        EncounterIDBox = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        EncounterDateBox = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        EncounterDiagnosisBox = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        EncounterProviderBox = new javax.swing.JTextField();
        EncountersCreateButton = new javax.swing.JButton();
        EncountersPreviousButton = new javax.swing.JButton();
        EncountersNextButton = new javax.swing.JButton();
        EncounterAddRecordButton = new javax.swing.JButton();
        EncounterCancelAddButton = new javax.swing.JButton();
        EncounterModifyCurrentButton = new javax.swing.JButton();
        EncounterSaveModifyButton = new javax.swing.JButton();
        EncounterCancelModifyButton = new javax.swing.JButton();
        EncounterTypeBox = new javax.swing.JComboBox();
        jScrollPane7 = new javax.swing.JScrollPane();
        EncounterNotesBox = new javax.swing.JTextArea();
        EncounterDelete = new javax.swing.JButton();
        EncounterErrorLabel = new javax.swing.JLabel();
        VitalSignsPanel = new javax.swing.JPanel();
        VitalPreviousButton = new javax.swing.JButton();
        VitalNextButton = new javax.swing.JButton();
        VitalNewButton = new javax.swing.JButton();
        VitalSaveNewButton = new javax.swing.JButton();
        VitalCancelNewButton = new javax.swing.JButton();
        VitalModifyCurrentButton = new javax.swing.JButton();
        VitalSaveModifyButton = new javax.swing.JButton();
        VitalCancelModifyButton = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        VitalIDBox = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        VitalFootBox = new javax.swing.JTextField();
        VitalInchBox = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        VitalWeightBox = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        VitalHRBox = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        VitalSystolicBox = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        VitalDiastolicBox = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        VitalTempBox = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        VitalDateBox = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        VitalListBox = new javax.swing.JList();
        jLabel55 = new javax.swing.JLabel();
        SysLabel = new javax.swing.JLabel();
        DiaLabel = new javax.swing.JLabel();
        VitalDelete = new javax.swing.JButton();
        MedicationsPanel = new javax.swing.JPanel();
        MedicationsPreviousButton = new javax.swing.JButton();
        MedicationsNextButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        MedicationIDBox = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        MedicationNameBox = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        DosageBox = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        DosageUnitsComboBox = new javax.swing.JComboBox();
        DosageFrequencyComboBox = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        MedicationStartDateBox = new javax.swing.JTextField();
        MedicationEndDateBox = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        InstructionsBox = new javax.swing.JTextArea();
        jLabel34 = new javax.swing.JLabel();
        PrescribedByBox = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        CurrentPreviousMedicationList = new javax.swing.JList();
        MedicationCreateButton = new javax.swing.JButton();
        MedicationAddRecordButton = new javax.swing.JButton();
        MedicationDeleteRecordButton = new javax.swing.JButton();
        MedicationModifyButton = new javax.swing.JButton();
        MedicationCancelModifyButton = new javax.swing.JButton();
        MedicationCancelNewButton = new javax.swing.JButton();
        MedicationSaveModifyButton = new javax.swing.JButton();
        CurrentMedicationButton = new javax.swing.JButton();
        PreviousMedicationButton = new javax.swing.JButton();
        UnitErrorLabel = new javax.swing.JLabel();
        FreqErrorLabel = new javax.swing.JLabel();
        LabsProceduresPanel = new javax.swing.JPanel();
        LabPreviousButton = new javax.swing.JButton();
        LabNextButton = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        LabIDBox = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        LabTestTypeComboBox = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        LabTestDescriptionBox = new javax.swing.JTextArea();
        LabTestDateTimeBox = new javax.swing.JTextField();
        LabCreateRecordButton = new javax.swing.JButton();
        LabAddRecordButton = new javax.swing.JButton();
        LabDeleteRecordButton = new javax.swing.JButton();
        LabModifyButton = new javax.swing.JButton();
        LabCancelModificationButton = new javax.swing.JButton();
        AllLabsProceduresButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        AllLabsProceduresList = new javax.swing.JList();
        jScrollPane8 = new javax.swing.JScrollPane();
        LabTestResultsBox = new javax.swing.JTextArea();
        LabsCancelNewButton = new javax.swing.JButton();
        LabSaveModifyButton = new javax.swing.JButton();
        TypeErrorLabel = new javax.swing.JLabel();
        ReportsPanel = new javax.swing.JPanel();
        ReportPatientBox = new javax.swing.JComboBox();
        ReportButton = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        GlobalNameLabel = new javax.swing.JLabel();
        GlobalAgeLabel = new javax.swing.JLabel();
        GlobalSexLabel = new javax.swing.JLabel();
        Search = new javax.swing.JLabel();
        DemoSearchBox = new javax.swing.JTextField();
        DemoSearchButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PatientDemographics.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                PatientDemographicsStateChanged(evt);
            }
        });

        jLabel17.setText("Contact number:");

        jLabel7.setText("Address:");

        jLabel18.setText("Notes:");

        CityBox.setText("CityBox");
        CityBox.setEnabled(false);

        PreviousButton.setText("Previous");
        PreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreviousButtonActionPerformed(evt);
            }
        });

        jLabel8.setText("City:");

        NextButton.setText("Next");
        NextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextButtonActionPerformed(evt);
            }
        });

        StateBox.setText("StateBox");
        StateBox.setEnabled(false);

        jLabel9.setText("ST:");

        ZipBox.setText("ZipBox");
        ZipBox.setEnabled(false);

        jLabel10.setText("Zip:");

        BirthDayBox.setText("BirthDayBox");
        BirthDayBox.setEnabled(false);

        PatientIDBox.setBackground(new java.awt.Color(240, 240, 240));
        PatientIDBox.setText("PatientIDBox");
        PatientIDBox.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        PatientIDBox.setDoubleBuffered(true);
        PatientIDBox.setEnabled(false);

        jLabel11.setText("Birth Date:");

        CreateRecordButton.setText("New Patient");
        CreateRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateRecordButtonActionPerformed(evt);
            }
        });

        AgeBox.setBackground(new java.awt.Color(240, 240, 240));
        AgeBox.setText("AgeBox");
        AgeBox.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        AgeBox.setEnabled(false);

        RecordNumBox.setText("RecordNumBox");
        RecordNumBox.setEnabled(false);

        AltRecordNumBox.setText("AltRecordNumBox");
        AltRecordNumBox.setEnabled(false);

        HomePhoneBox.setText("HomePhoneBox");
        HomePhoneBox.setEnabled(false);

        LastNameBox.setText("LastNameBox");
        LastNameBox.setEnabled(false);

        jLabel13.setText("Home/Work:");

        FirstNameBox.setText("FirstNameBox");
        FirstNameBox.setEnabled(false);

        WorkPhoneBox.setText("WorkPhoneBox");
        WorkPhoneBox.setEnabled(false);

        MiddleInitBox.setText("MiddleInitBox");
        MiddleInitBox.setEnabled(false);

        AddressBox.setText("AddressBox");
        AddressBox.setEnabled(false);

        EmailBox.setText("EmailBox");
        EmailBox.setEnabled(false);

        jLabel1.setText("Patient ID:");

        jLabel15.setText("Email:");

        jLabel2.setText("Med. Recod Number:");

        NextOfKinBox.setText("NextOfKinBox");
        NextOfKinBox.setEnabled(false);

        jLabel3.setText("Alt. Med. Record Number:");

        jLabel16.setText("Next of kin:");

        jLabel4.setText("Last Name:");

        KinContactBox.setText("KinContactBox");
        KinContactBox.setEnabled(false);

        jLabel5.setText("First Name:");

        jLabel6.setText("MI:");

        DemoAddRecordButton.setText("Save New");
        DemoAddRecordButton.setEnabled(false);
        DemoAddRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DemoAddRecordButtonActionPerformed(evt);
            }
        });

        SexBox.setText("SexBox");
        SexBox.setToolTipText("");
        SexBox.setEnabled(false);

        jLabel25.setLabelFor(SexBox);
        jLabel25.setText("Sex:");

        jLabel26.setForeground(new java.awt.Color(255, 0, 0));
        jLabel26.setText("EMERGENCY CONTACT INFORMATION");

        ModifyDemoButton.setText("Modify Current");
        ModifyDemoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifyDemoButtonActionPerformed(evt);
            }
        });

        CancelModifyDemoButton.setText("Cancel Modify");
        CancelModifyDemoButton.setEnabled(false);
        CancelModifyDemoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelModifyDemoButtonActionPerformed(evt);
            }
        });

        CancelAddButton.setBackground(new java.awt.Color(204, 255, 204));
        CancelAddButton.setText("Cancel New");
        CancelAddButton.setEnabled(false);
        CancelAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelAddButtonActionPerformed(evt);
            }
        });

        SaveModifyButton.setText("Save Modify");
        SaveModifyButton.setEnabled(false);
        SaveModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveModifyButtonActionPerformed(evt);
            }
        });

        NotesBox.setColumns(20);
        NotesBox.setRows(5);
        NotesBox.setEnabled(false);
        jScrollPane6.setViewportView(NotesBox);

        DemoDelete.setText("Delete Patient");
        DemoDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DemoDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PatientsDemoPanelLayout = new javax.swing.GroupLayout(PatientsDemoPanel);
        PatientsDemoPanel.setLayout(PatientsDemoPanelLayout);
        PatientsDemoPanelLayout.setHorizontalGroup(
            PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                        .addComponent(PreviousButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(CreateRecordButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DemoAddRecordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CancelAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ModifyDemoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SaveModifyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CancelModifyDemoButton))
                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(AddressBox)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PatientsDemoPanelLayout.createSequentialGroup()
                                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                                .addComponent(BirthDayBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(AgeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(27, 27, 27)
                                                .addComponent(jLabel25)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(SexBox))
                                            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                                .addComponent(CityBox, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(StateBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel10)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ZipBox, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))))
                            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PatientIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RecordNumBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AltRecordNumBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(EmailBox, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                        .addComponent(HomePhoneBox, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(WorkPhoneBox, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(FirstNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4))
                                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                        .addComponent(MiddleInitBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(LastNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel26)
                            .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PatientsDemoPanelLayout.createSequentialGroup()
                                    .addComponent(jLabel17)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(KinContactBox))
                                .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                    .addComponent(jLabel16)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(NextOfKinBox, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                                .addGap(282, 282, 282)
                                .addComponent(DemoDelete)))))
                .addGap(0, 605, Short.MAX_VALUE))
        );
        PatientsDemoPanelLayout.setVerticalGroup(
            PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PreviousButton)
                    .addComponent(CreateRecordButton)
                    .addComponent(DemoAddRecordButton)
                    .addComponent(ModifyDemoButton)
                    .addComponent(CancelModifyDemoButton)
                    .addComponent(CancelAddButton)
                    .addComponent(SaveModifyButton)
                    .addComponent(NextButton))
                .addGap(50, 50, 50)
                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(FirstNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MiddleInitBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LastNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(RecordNumBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(AltRecordNumBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(PatientIDBox)
                                .addComponent(jLabel1)))
                        .addGap(18, 18, 18)
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(AddressBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(CityBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(StateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(ZipBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(BirthDayBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AgeBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)
                            .addComponent(SexBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(HomePhoneBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(WorkPhoneBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(EmailBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(7, 7, 7)
                        .addComponent(jScrollPane6)))
                .addGap(34, 34, 34)
                .addComponent(jLabel26)
                .addGap(14, 14, 14)
                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(NextOfKinBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PatientsDemoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(KinContactBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PatientsDemoPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(DemoDelete)))
                .addGap(29, 29, 29)
                .addComponent(jLabel14)
                .addGap(136, 136, 136))
        );

        PatientDemographics.addTab("Patient Demographics", PatientsDemoPanel);

        jLabel19.setText("Encounter ID:");

        EncounterIDBox.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        EncounterIDBox.setEnabled(false);
        EncounterIDBox.setName(""); // NOI18N

        jLabel20.setText("Encounter Date:");

        EncounterDateBox.setAutoscrolls(false);
        EncounterDateBox.setEnabled(false);
        EncounterDateBox.setName("EncounterDateBox"); // NOI18N

        jLabel21.setText("Encounter Type:");

        jLabel22.setText("Notes:");

        EncounterDiagnosisBox.setEnabled(false);

        jLabel23.setText("Diagnosis Description:");

        jLabel24.setText("Provider:");

        EncounterProviderBox.setEnabled(false);

        EncountersCreateButton.setText("New Encounter");
        EncountersCreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncountersCreateButtonActionPerformed(evt);
            }
        });

        EncountersPreviousButton.setText("Previous");
        EncountersPreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncountersPreviousButtonActionPerformed(evt);
            }
        });

        EncountersNextButton.setText("Next");
        EncountersNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncountersNextButtonActionPerformed(evt);
            }
        });

        EncounterAddRecordButton.setText("Save Encounter");
        EncounterAddRecordButton.setEnabled(false);
        EncounterAddRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncounterAddRecordButtonActionPerformed(evt);
            }
        });

        EncounterCancelAddButton.setText("Cancel New");
        EncounterCancelAddButton.setEnabled(false);
        EncounterCancelAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncounterCancelAddButtonActionPerformed(evt);
            }
        });

        EncounterModifyCurrentButton.setText("Modify Current");
        EncounterModifyCurrentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncounterModifyCurrentButtonActionPerformed(evt);
            }
        });

        EncounterSaveModifyButton.setText("Save Modify");
        EncounterSaveModifyButton.setEnabled(false);
        EncounterSaveModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncounterSaveModifyButtonActionPerformed(evt);
            }
        });

        EncounterCancelModifyButton.setText("Cancel Modify");
        EncounterCancelModifyButton.setEnabled(false);
        EncounterCancelModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncounterCancelModifyButtonActionPerformed(evt);
            }
        });

        EncounterTypeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Outpatient", "Inpatient", "Outpatient Lab", "Outpatient Procedure", "Follow-up", "Routine", "Emergency", "Other" }));
        EncounterTypeBox.setEnabled(false);

        EncounterNotesBox.setColumns(20);
        EncounterNotesBox.setRows(5);
        EncounterNotesBox.setEnabled(false);
        jScrollPane7.setViewportView(EncounterNotesBox);

        EncounterDelete.setText("Delete Record");
        EncounterDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncounterDeleteActionPerformed(evt);
            }
        });

        EncounterErrorLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        EncounterErrorLabel.setForeground(new java.awt.Color(255, 0, 51));
        EncounterErrorLabel.setText("*");
        EncounterErrorLabel.setFocusable(false);
        EncounterErrorLabel.setOpaque(true);

        javax.swing.GroupLayout EncountersPanelLayout = new javax.swing.GroupLayout(EncountersPanel);
        EncountersPanel.setLayout(EncountersPanelLayout);
        EncountersPanelLayout.setHorizontalGroup(
            EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EncountersPanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EncountersPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(EncountersPanelLayout.createSequentialGroup()
                        .addGroup(EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, EncountersPanelLayout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EncounterIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EncounterDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EncounterTypeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(EncounterErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 823, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EncountersPanelLayout.createSequentialGroup()
                        .addGroup(EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(EncountersPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(EncounterDelete))
                            .addGroup(EncountersPanelLayout.createSequentialGroup()
                                .addGroup(EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(EncountersPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(EncounterDiagnosisBox)
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(EncountersPanelLayout.createSequentialGroup()
                                        .addComponent(EncountersPreviousButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(EncountersNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(EncountersCreateButton)
                                        .addGap(2, 2, 2)
                                        .addComponent(EncounterAddRecordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(EncounterCancelAddButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(EncounterModifyCurrentButton)
                                        .addGap(4, 4, 4)))
                                .addGroup(EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(EncounterProviderBox, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(EncountersPanelLayout.createSequentialGroup()
                                        .addComponent(EncounterSaveModifyButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(EncounterCancelModifyButton)))))
                        .addGap(592, 592, 592))))
        );
        EncountersPanelLayout.setVerticalGroup(
            EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EncountersPanelLayout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EncountersPreviousButton)
                    .addComponent(EncountersNextButton)
                    .addComponent(EncountersCreateButton)
                    .addComponent(EncounterAddRecordButton)
                    .addComponent(EncounterCancelAddButton)
                    .addComponent(EncounterModifyCurrentButton)
                    .addComponent(EncounterSaveModifyButton)
                    .addComponent(EncounterCancelModifyButton))
                .addGap(18, 35, Short.MAX_VALUE)
                .addGroup(EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(EncounterIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(EncounterDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(EncounterTypeBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EncounterErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(EncountersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(EncounterDiagnosisBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(EncounterProviderBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(EncounterDelete)
                .addGap(273, 273, 273))
        );

        PatientDemographics.addTab("Encounters", EncountersPanel);

        VitalPreviousButton.setText("Previous");
        VitalPreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalPreviousButtonActionPerformed(evt);
            }
        });

        VitalNextButton.setText("Next");
        VitalNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalNextButtonActionPerformed(evt);
            }
        });

        VitalNewButton.setText("New Vital Record");
        VitalNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalNewButtonActionPerformed(evt);
            }
        });

        VitalSaveNewButton.setText("Save New");
        VitalSaveNewButton.setEnabled(false);
        VitalSaveNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalSaveNewButtonActionPerformed(evt);
            }
        });

        VitalCancelNewButton.setText("Cancel New");
        VitalCancelNewButton.setEnabled(false);
        VitalCancelNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalCancelNewButtonActionPerformed(evt);
            }
        });

        VitalModifyCurrentButton.setText("Modify Current");
        VitalModifyCurrentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalModifyCurrentButtonActionPerformed(evt);
            }
        });

        VitalSaveModifyButton.setText("Save Modify");
        VitalSaveModifyButton.setEnabled(false);
        VitalSaveModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalSaveModifyButtonActionPerformed(evt);
            }
        });

        VitalCancelModifyButton.setText("Cancel Modify");
        VitalCancelModifyButton.setEnabled(false);
        VitalCancelModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalCancelModifyButtonActionPerformed(evt);
            }
        });

        jLabel32.setText("Vital ID:");

        VitalIDBox.setEnabled(false);

        jLabel42.setText("Height:");

        VitalFootBox.setEnabled(false);

        VitalInchBox.setEnabled(false);

        jLabel43.setText("ft.");

        jLabel44.setText("in.");

        jLabel45.setText("Weight:");

        VitalWeightBox.setEnabled(false);

        jLabel46.setText("lbs.");

        jLabel47.setText("Heart Rate:");

        VitalHRBox.setEnabled(false);

        jLabel48.setText("bpm");

        jLabel49.setText("Blood Pressure:");

        VitalSystolicBox.setEnabled(false);
        VitalSystolicBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                VitalSystolicBoxFocusLost(evt);
            }
        });

        jLabel50.setText("/");

        VitalDiastolicBox.setEnabled(false);
        VitalDiastolicBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                VitalDiastolicBoxFocusLost(evt);
            }
        });

        jLabel51.setText("Temperature:");

        VitalTempBox.setEnabled(false);

        jLabel54.setText("Vital Date/Time:");

        VitalDateBox.setEnabled(false);

        VitalListBox.setEnabled(false);
        jScrollPane4.setViewportView(VitalListBox);

        jLabel55.setText("Previous Vitals:");

        VitalDelete.setText("Delete Record");
        VitalDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VitalDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout VitalSignsPanelLayout = new javax.swing.GroupLayout(VitalSignsPanel);
        VitalSignsPanel.setLayout(VitalSignsPanelLayout);
        VitalSignsPanelLayout.setHorizontalGroup(
            VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                        .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, VitalSignsPanelLayout.createSequentialGroup()
                                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel55)
                                    .addComponent(jLabel49))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(SysLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(VitalSystolicBox, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel50)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(DiaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(VitalDiastolicBox, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(VitalTempBox, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, VitalSignsPanelLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, VitalSignsPanelLayout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(VitalFootBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel43)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(VitalInchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel54)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(VitalDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel44)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel45)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(VitalWeightBox, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel46)
                                        .addGap(36, 36, 36)
                                        .addComponent(jLabel47)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(VitalHRBox, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel48)))))
                        .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                                .addGap(164, 164, 164)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(VitalDelete)
                                    .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel53)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel52))))))
                    .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                        .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, VitalSignsPanelLayout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(VitalIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                                .addComponent(VitalPreviousButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(VitalNextButton)
                                .addGap(46, 46, 46)
                                .addComponent(VitalNewButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(VitalSaveNewButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(VitalCancelNewButton)
                        .addGap(49, 49, 49)
                        .addComponent(VitalModifyCurrentButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(VitalSaveModifyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(VitalCancelModifyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(559, Short.MAX_VALUE))
        );
        VitalSignsPanelLayout.setVerticalGroup(
            VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(VitalPreviousButton)
                    .addComponent(VitalNextButton)
                    .addComponent(VitalNewButton)
                    .addComponent(VitalSaveNewButton)
                    .addComponent(VitalCancelNewButton)
                    .addComponent(VitalModifyCurrentButton)
                    .addComponent(VitalSaveModifyButton)
                    .addComponent(VitalCancelModifyButton))
                .addGap(34, 34, 34)
                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(VitalIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54)
                    .addComponent(VitalDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(VitalFootBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(VitalInchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43)
                            .addComponent(jLabel44)
                            .addComponent(jLabel45)
                            .addComponent(VitalWeightBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47)
                            .addComponent(VitalHRBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48)))
                    .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(VitalSystolicBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50)
                    .addComponent(VitalDiastolicBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51)
                    .addComponent(VitalTempBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(VitalSignsPanelLayout.createSequentialGroup()
                        .addGroup(VitalSignsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DiaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                            .addComponent(SysLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addComponent(jLabel55)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(VitalDelete))
                .addContainerGap(231, Short.MAX_VALUE))
        );

        PatientDemographics.addTab("Vital Signs", VitalSignsPanel);

        MedicationsPreviousButton.setText("Previous");
        MedicationsPreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationsPreviousButtonActionPerformed(evt);
            }
        });

        MedicationsNextButton.setText("Next");
        MedicationsNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationsNextButtonActionPerformed(evt);
            }
        });

        jLabel12.setText("Med. ID:");

        MedicationIDBox.setEnabled(false);

        jLabel27.setText("Med. Name:");

        MedicationNameBox.setEnabled(false);

        jLabel28.setText("Dosage:");

        DosageBox.setEnabled(false);

        jLabel29.setText("Dosage Units:");

        jLabel30.setText("Dosage Frequency:");

        DosageUnitsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Units", "mg", "mL", "tsp", "tbsp", "gtt" }));
        DosageUnitsComboBox.setEnabled(false);

        DosageFrequencyComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Frequency", "QID", "BID", "TID", "TIW", "SID" }));
        DosageFrequencyComboBox.setEnabled(false);

        jLabel31.setText("Med. Start/End Date:");

        MedicationStartDateBox.setEnabled(false);

        MedicationEndDateBox.setEnabled(false);

        jLabel33.setText("Instructions/Notes:");

        InstructionsBox.setColumns(20);
        InstructionsBox.setRows(5);
        InstructionsBox.setEnabled(false);
        jScrollPane1.setViewportView(InstructionsBox);

        jLabel34.setText("Prescribed By:");

        PrescribedByBox.setEnabled(false);

        CurrentPreviousMedicationList.setFocusable(false);
        jScrollPane2.setViewportView(CurrentPreviousMedicationList);

        MedicationCreateButton.setText("New Medication");
        MedicationCreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationCreateButtonActionPerformed(evt);
            }
        });

        MedicationAddRecordButton.setText("Save New");
        MedicationAddRecordButton.setEnabled(false);
        MedicationAddRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationAddRecordButtonActionPerformed(evt);
            }
        });

        MedicationDeleteRecordButton.setText("Delete Record");
        MedicationDeleteRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationDeleteRecordButtonActionPerformed(evt);
            }
        });

        MedicationModifyButton.setText("Modify Medication");
        MedicationModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationModifyButtonActionPerformed(evt);
            }
        });

        MedicationCancelModifyButton.setText("Cancel Modify");
        MedicationCancelModifyButton.setEnabled(false);
        MedicationCancelModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationCancelModifyButtonActionPerformed(evt);
            }
        });

        MedicationCancelNewButton.setText("Cancel New");
        MedicationCancelNewButton.setEnabled(false);
        MedicationCancelNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationCancelNewButtonActionPerformed(evt);
            }
        });

        MedicationSaveModifyButton.setText("Save Modify");
        MedicationSaveModifyButton.setEnabled(false);
        MedicationSaveModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MedicationSaveModifyButtonActionPerformed(evt);
            }
        });

        CurrentMedicationButton.setText("Current Medication");
        CurrentMedicationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentMedicationButtonActionPerformed(evt);
            }
        });

        PreviousMedicationButton.setText("Previous Medication");
        PreviousMedicationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreviousMedicationButtonActionPerformed(evt);
            }
        });

        UnitErrorLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        UnitErrorLabel.setForeground(new java.awt.Color(255, 0, 51));
        UnitErrorLabel.setText("*");
        UnitErrorLabel.setFocusable(false);
        UnitErrorLabel.setOpaque(true);

        FreqErrorLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        FreqErrorLabel.setForeground(new java.awt.Color(255, 0, 51));
        FreqErrorLabel.setText("*");
        FreqErrorLabel.setFocusable(false);
        FreqErrorLabel.setOpaque(true);

        javax.swing.GroupLayout MedicationsPanelLayout = new javax.swing.GroupLayout(MedicationsPanel);
        MedicationsPanel.setLayout(MedicationsPanelLayout);
        MedicationsPanelLayout.setHorizontalGroup(
            MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MedicationsPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MedicationsPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(249, 249, 249)
                        .addComponent(MedicationDeleteRecordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MedicationsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(MedicationStartDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MedicationEndDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MedicationsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PrescribedByBox, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PreviousMedicationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CurrentMedicationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(MedicationsPanelLayout.createSequentialGroup()
                        .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(MedicationsPanelLayout.createSequentialGroup()
                                .addComponent(MedicationsPreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MedicationsNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(MedicationCreateButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, MedicationsPanelLayout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DosageFrequencyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, MedicationsPanelLayout.createSequentialGroup()
                                .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(MedicationsPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(DosageBox))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, MedicationsPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(MedicationIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel27)))
                                .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(MedicationsPanelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(MedicationNameBox))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MedicationsPanelLayout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(jLabel29)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(DosageUnitsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MedicationsPanelLayout.createSequentialGroup()
                                .addComponent(MedicationAddRecordButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MedicationCancelNewButton)
                                .addGap(18, 18, 18)
                                .addComponent(MedicationModifyButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MedicationSaveModifyButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MedicationCancelModifyButton))
                            .addGroup(MedicationsPanelLayout.createSequentialGroup()
                                .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(FreqErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(UnitErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel33)))))))
        );
        MedicationsPanelLayout.setVerticalGroup(
            MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MedicationsPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MedicationAddRecordButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(MedicationsPreviousButton)
                        .addComponent(MedicationsNextButton)
                        .addComponent(MedicationCreateButton)
                        .addComponent(MedicationCancelNewButton)
                        .addComponent(MedicationModifyButton)
                        .addComponent(MedicationCancelModifyButton)
                        .addComponent(MedicationSaveModifyButton)))
                .addGap(29, 29, 29)
                .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(MedicationIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel33)
                    .addComponent(MedicationNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MedicationsPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(MedicationDeleteRecordButton))
                    .addGroup(MedicationsPanelLayout.createSequentialGroup()
                        .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(DosageBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)
                            .addComponent(DosageUnitsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UnitErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(DosageFrequencyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FreqErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(MedicationStartDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MedicationEndDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(MedicationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(PrescribedByBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addComponent(PreviousMedicationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CurrentMedicationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167))
        );

        PatientDemographics.addTab("Medications", MedicationsPanel);

        LabsProceduresPanel.setEnabled(false);

        LabPreviousButton.setText("Previous");
        LabPreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabPreviousButtonActionPerformed(evt);
            }
        });

        LabNextButton.setText("Next");
        LabNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabNextButtonActionPerformed(evt);
            }
        });

        jLabel36.setText("Lab ID:");

        LabIDBox.setEnabled(false);

        jLabel37.setText("Test Type:");

        jLabel38.setText("Test Description:");

        jLabel39.setText("Test Results:");

        jLabel40.setText("Test Date/Time:");

        LabTestTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Type", "Blood", "Urine", "CAT Scan", "Ultrasound", "TB Skin", "Throat Swab" }));
        LabTestTypeComboBox.setEnabled(false);

        LabTestDescriptionBox.setColumns(20);
        LabTestDescriptionBox.setRows(5);
        LabTestDescriptionBox.setEnabled(false);
        jScrollPane3.setViewportView(LabTestDescriptionBox);

        LabTestDateTimeBox.setEnabled(false);

        LabCreateRecordButton.setText("New Lab/Procedure");
        LabCreateRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabCreateRecordButtonActionPerformed(evt);
            }
        });

        LabAddRecordButton.setText("Save New");
        LabAddRecordButton.setEnabled(false);
        LabAddRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabAddRecordButtonActionPerformed(evt);
            }
        });

        LabDeleteRecordButton.setText("Delete Record");
        LabDeleteRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabDeleteRecordButtonActionPerformed(evt);
            }
        });

        LabModifyButton.setText("Modify Lab/Procedure");
        LabModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabModifyButtonActionPerformed(evt);
            }
        });

        LabCancelModificationButton.setText("Cancel Modify");
        LabCancelModificationButton.setEnabled(false);
        LabCancelModificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabCancelModificationButtonActionPerformed(evt);
            }
        });

        AllLabsProceduresButton.setText("All Labs/Procedures");
        AllLabsProceduresButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllLabsProceduresButtonActionPerformed(evt);
            }
        });

        jScrollPane5.setViewportView(AllLabsProceduresList);

        LabTestResultsBox.setColumns(20);
        LabTestResultsBox.setRows(5);
        LabTestResultsBox.setEnabled(false);
        jScrollPane8.setViewportView(LabTestResultsBox);

        LabsCancelNewButton.setText("Cancel New");
        LabsCancelNewButton.setEnabled(false);
        LabsCancelNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabsCancelNewButtonActionPerformed(evt);
            }
        });

        LabSaveModifyButton.setText("Save Modify");
        LabSaveModifyButton.setEnabled(false);
        LabSaveModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabSaveModifyButtonActionPerformed(evt);
            }
        });

        TypeErrorLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        TypeErrorLabel.setForeground(new java.awt.Color(255, 0, 51));
        TypeErrorLabel.setText("*");
        TypeErrorLabel.setOpaque(true);

        javax.swing.GroupLayout LabsProceduresPanelLayout = new javax.swing.GroupLayout(LabsProceduresPanel);
        LabsProceduresPanel.setLayout(LabsProceduresPanelLayout);
        LabsProceduresPanelLayout.setHorizontalGroup(
            LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                        .addComponent(LabPreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LabNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LabCreateRecordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabAddRecordButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabsCancelNewButton)
                        .addGap(18, 18, 18)
                        .addComponent(LabModifyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabSaveModifyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabCancelModificationButton))
                    .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                        .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LabIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel37)
                                .addGap(14, 14, 14)
                                .addComponent(LabTestTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TypeErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LabTestDateTimeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21)
                        .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(AllLabsProceduresButton)
                            .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                .addGap(307, 307, 307))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LabsProceduresPanelLayout.createSequentialGroup()
                                .addComponent(LabDeleteRecordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(202, 202, 202)))))
                .addContainerGap(351, Short.MAX_VALUE))
        );
        LabsProceduresPanelLayout.setVerticalGroup(
            LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabPreviousButton)
                    .addComponent(LabNextButton)
                    .addComponent(LabCreateRecordButton)
                    .addComponent(LabAddRecordButton)
                    .addComponent(LabModifyButton)
                    .addComponent(LabsCancelNewButton)
                    .addComponent(LabCancelModificationButton)
                    .addComponent(LabSaveModifyButton))
                .addGap(30, 30, 30)
                .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(LabIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(LabTestTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TypeErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AllLabsProceduresButton))
                .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(LabTestDateTimeBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(LabsProceduresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39)))
                    .addGroup(LabsProceduresPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LabDeleteRecordButton)))
                .addContainerGap(266, Short.MAX_VALUE))
        );

        PatientDemographics.addTab("Labs/Procedures", LabsProceduresPanel);

        ReportPatientBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ReportButton.setText("Print Report");
        ReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReportButtonActionPerformed(evt);
            }
        });

        jLabel35.setText("Select Patient:");

        javax.swing.GroupLayout ReportsPanelLayout = new javax.swing.GroupLayout(ReportsPanel);
        ReportsPanel.setLayout(ReportsPanelLayout);
        ReportsPanelLayout.setHorizontalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel35)
                .addGap(60, 60, 60)
                .addComponent(ReportPatientBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125)
                .addComponent(ReportButton)
                .addContainerGap(1025, Short.MAX_VALUE))
        );
        ReportsPanelLayout.setVerticalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ReportPatientBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ReportButton)
                    .addComponent(jLabel35))
                .addContainerGap(509, Short.MAX_VALUE))
        );

        PatientDemographics.addTab("Reports", ReportsPanel);

        GlobalNameLabel.setBackground(new java.awt.Color(255, 255, 255));
        GlobalNameLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        GlobalNameLabel.setForeground(new java.awt.Color(255, 51, 0));
        GlobalNameLabel.setText("label");

        GlobalAgeLabel.setBackground(new java.awt.Color(255, 255, 255));
        GlobalAgeLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        GlobalAgeLabel.setForeground(new java.awt.Color(255, 51, 0));
        GlobalAgeLabel.setText("jLabel19");

        GlobalSexLabel.setBackground(new java.awt.Color(255, 255, 255));
        GlobalSexLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        GlobalSexLabel.setForeground(new java.awt.Color(255, 51, 0));
        GlobalSexLabel.setText("jLabel19");

        Search.setText("Patient Search:");

        DemoSearchBox.setBackground(new java.awt.Color(240, 240, 240));
        DemoSearchBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DemoSearchBoxActionPerformed(evt);
            }
        });

        DemoSearchButton.setText("Search");
        DemoSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DemoSearchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(GlobalNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(GlobalAgeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(GlobalSexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(Search)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DemoSearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DemoSearchButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PatientDemographics)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(GlobalSexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GlobalAgeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GlobalNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Search)
                    .addComponent(DemoSearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DemoSearchButton))
                .addGap(26, 26, 26)
                .addComponent(PatientDemographics, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(247, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    //Inserts an encounter list into an array for the current patient
    private void getEncounter()
    {
        
        //Creates a new blank array list for the encounters
        PatientEncounter=new ArrayList<>();
        
        //Gets patient id for use in the sql statement
        int tempid=Incoming.GetID();
        
        String SQL="SELECT * FROM Encounters WHERE Patient_ID="+tempid+
                " ORDER BY Encounter_Date DESC";
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(SQL);)
        {    
            
            //Loops through the records and inserts them into an Array List
            while(rs.next())
            {
                int ID=rs.getInt("Encounter_ID");
                //Converts MYSQL Date into Gregorian Calendar format
                GregorianCalendar Date=new GregorianCalendar();
                Date.setTime(rs.getTimestamp("Encounter_Date"));
                
                String Type=rs.getString("Encounter_Type");
                String Notes=rs.getString("Notes");
                String Description=rs.getString("Diagnosis_Description");
                String Provider=rs.getString("Provider");
                
                
                Encounters p= new Encounters(ID, Date, Type, Notes, Description, Provider);
                PatientEncounter.add(p);                 
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Sets the max records number to the count of all records in the database
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SELECT Count(*) FROM Encounters WHERE Patient_ID="+tempid);)
        {    
            while(rs.next())
            {
            
                MaxEncounters=rs.getInt("Count(*)"); 
                MaxEncounters--;
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
    }

    //Activates on tab change to fire the correct code
    private void PatientDemographicsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_PatientDemographicsStateChanged
        
        //Get the current index of the selected tab and call the correct functions
        int index=PatientDemographics.getSelectedIndex();
        try
        {
        switch(index)
        {
            case 0://getPatient(CurrentPatient);
                ClearPatient();
                clearDemoValidation();
                DisplayPatient();
                break;
            case 1:getEncounter();
                CurrentEncounter=0;
                ClearEncounters();
                clearEncounterValidation();
                DisplayEncounter(CurrentEncounter);
                break;
            case 2:getVitals();
                CurrentVital=0;
                ClearVitals();
                clearVitalValidation();
                DisplayVital(CurrentVital);
                break;
            case 3:getMedication();
                CurrentMedication=0;                
                ClearMedication();
                clearMedValidation();
                DisplayMedication(CurrentMedication);
                break;
            case 4:getLab();
                CurrentLab=0;
                ClearLabs();
                clearLabValidation();
                DisplayLab(CurrentLab);
                break;
            case 5:Reports();
                break;
            default:break;
        }
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_PatientDemographicsStateChanged

   
//Clear Demographic tab validation
    private void clearDemoValidation()
{
    RecordNumBox.setBackground(Color.white);
    AltRecordNumBox.setBackground(Color.white);
    SexBox.setBackground(Color.white);
    LastNameBox.setBackground(Color.white);
    FirstNameBox.setBackground(Color.white);
    AddressBox.setBackground(Color.white);
    CityBox.setBackground(Color.white);
    StateBox.setBackground(Color.white);
    ZipBox.setBackground(Color.white);
    BirthDayBox.setBackground(Color.white);
    NextOfKinBox.setBackground(Color.white);
}
//Clear Encounter tab validation
private void clearEncounterValidation()
{
    EncounterIDBox.setBackground(Color.white);
    EncounterDateBox.setBackground(Color.white);
    EncounterErrorLabel.setVisible(false);
    EncounterNotesBox.setBackground(Color.white);
    EncounterDiagnosisBox.setBackground(Color.white);
    EncounterProviderBox.setBackground(Color.white);
}
//clear vital validateion
private void clearVitalValidation()
{
    VitalHRBox.setBackground(Color.white);
    VitalSystolicBox.setBackground(Color.white);
    VitalDiastolicBox.setBackground(Color.white);
    VitalDateBox.setBackground(Color.white);    
}
//Clear Lab tab validation
private void clearLabValidation()
{
    LabTestResultsBox.setBackground(Color.white);
    LabTestDescriptionBox.setBackground(Color.white);
    LabTestDateTimeBox.setBackground(Color.white);
    LabTestTypeComboBox.setBackground(Color.white);
    TypeErrorLabel.setVisible(false);
}
//Clear Medications tab validation
private void clearMedValidation()
{
    PrescribedByBox.setBackground(Color.white);
    MedicationStartDateBox.setBackground(Color.white);
    MedicationEndDateBox.setBackground(Color.white);
    DosageFrequencyComboBox.setBackground(Color.white);
    DosageUnitsComboBox.setBackground(Color.white);
    DosageBox.setBackground(Color.white);
    MedicationNameBox.setBackground(Color.white);
    UnitErrorLabel.setVisible(false);
    FreqErrorLabel.setVisible(false);
}
//Validate Demographic tab data
private boolean validateDemo()
{   //set flag
    boolean flag=true;
    //check test boxes in reverse order/set focus/set flag
    if (NextOfKinBox.getText().length() == 0)
    {
        NextOfKinBox.setBackground(Color.PINK);
        NextOfKinBox.requestFocus();
        flag = false;
    }
    if (SexBox.getText().length() == 0)
    {
        SexBox.setBackground(Color.PINK);
        SexBox.requestFocus();
        flag = false;
    }
    if (BirthDayBox.getText().length() == 0)
    {
        BirthDayBox.setBackground(Color.PINK);
        BirthDayBox.requestFocus();
        flag = false;
    }
    try
    {
        int z = Integer.parseInt(ZipBox.getText());
    }
        catch(NumberFormatException e)
            {
            ZipBox.setBackground(Color.PINK);
            ZipBox.requestFocus();
            flag = false;
            }      
    if (StateBox.getText().length() == 0)
    {
        StateBox.setBackground(Color.PINK);
        StateBox.requestFocus();
        flag = false;
    }
    if (CityBox.getText().length() == 0)
    {
        CityBox.setBackground(Color.PINK);
        CityBox.requestFocus();
        flag = false;
    }
    if (AddressBox.getText().length() == 0)
    {
        AddressBox.setBackground(Color.PINK);
        AddressBox.requestFocus();
        flag = false;
    }
    
    if(RecordNumBox.getText().length() == 0)
       {
           RecordNumBox.setBackground(Color.PINK);
           flag = false;
       }
    
    if (LastNameBox.getText().length() == 0)
    {
        LastNameBox.setBackground(Color.PINK);
        LastNameBox.requestFocus();
        flag = false;
    }
    if (FirstNameBox.getText().length() == 0)
    {
        FirstNameBox.setBackground(Color.PINK);
        FirstNameBox.requestFocus();
        flag = false;
    }
    if (flag == false)
        JOptionPane.showMessageDialog(this,"ONE OR MORE VALUES ARE INCORRECT","ENTRY ERROR",JOptionPane.ERROR_MESSAGE);
                return flag;
}
//validate Encounter tab data
private boolean validateEncounter()
{   //set flag
    boolean flag = true;
    //check test boxes in reverse order/set focus/set flag
    if (EncounterProviderBox.getText().length() == 0)
    {
        EncounterProviderBox.setBackground(Color.PINK);
        EncounterProviderBox.requestFocus();
        flag = false;
    }
    if (EncounterTypeBox.getSelectedIndex()== -1)
    {
        EncounterErrorLabel.setVisible(true);
        EncounterTypeBox.requestFocus();
        flag = false;
    }
    if (EncounterDateBox.getText().length() == 0)
    {
        EncounterDateBox.setBackground(Color.PINK);
        EncounterDateBox.requestFocus();
        flag = false;
    }
    if (flag == false)
        JOptionPane.showMessageDialog(this,"ONE OR MORE VALUES ARE INCORRECT","ENTRY ERROR",JOptionPane.ERROR_MESSAGE);
            
     return flag;
}
//validate Vital tab data
private boolean validateVital()
{
    //set flag
    boolean flag = true;
    //check test boxes in reverse order/set focus/set flag
    if (VitalDiastolicBox.getText().length() == 0)
    {
        VitalDiastolicBox.setBackground(Color.PINK);
        VitalDiastolicBox.requestFocus();
        flag = false;
    }
    if (VitalSystolicBox.getText().length() == 0)
    {
        VitalSystolicBox.setBackground(Color.PINK);
        VitalSystolicBox.requestFocus();
        flag = false;
    }
    if (VitalHRBox.getText().length() == 0)
    {
        VitalHRBox.setBackground(Color.PINK);
        VitalHRBox.requestFocus();
        flag = false;
    }
    if (VitalDateBox.getText().length() == 0)
    {
        VitalDateBox.setBackground(Color.PINK);
        VitalDateBox.requestFocus();
        flag = false;
    }  
    if (flag == false)
        JOptionPane.showMessageDialog(this,"ONE OR MORE VALUES ARE INCORRECT","ENTRY ERROR",JOptionPane.ERROR_MESSAGE);
            
    return flag;
}
//validate Medication tab data
private boolean validateMedication()
{   //set flag
    boolean flag = true;
    //check test boxes in reverse order/set focus/set flag
    if (MedicationStartDateBox.getText().length() == 0)
    {
        MedicationStartDateBox.setBackground(Color.PINK);
        MedicationStartDateBox.requestFocus();
        flag = false;
    }
     if (DosageFrequencyComboBox.getSelectedIndex() == 0)
    {
        FreqErrorLabel.setVisible(true);
        DosageFrequencyComboBox.requestFocus();
        flag = false;
    }
    if (DosageUnitsComboBox.getSelectedIndex() == 0)
    {
        UnitErrorLabel.setVisible(true);
        DosageUnitsComboBox.requestFocus();
        flag = false;
    }
    if (DosageBox.getText().length() == 0)
    {
        DosageBox.setBackground(Color.PINK);
        DosageBox.requestFocus();
        flag = false;
    }
    if (MedicationNameBox.getText().length() == 0)
    {
        MedicationNameBox.setBackground(Color.PINK);
        MedicationNameBox.requestFocus();
        flag = false;
    }
    if (flag == false)
        JOptionPane.showMessageDialog(this,"ONE OR MORE VALUES ARE INCORRECT","ENTRY ERROR",JOptionPane.ERROR_MESSAGE);
            
     return flag;
}
//validate Lab tab data
private boolean validateLab()
{   //set flag
    boolean flag = true;
    //check test boxes in reverse order/set focus/set flag
    if (LabTestTypeComboBox.getSelectedIndex() == 0)
    {
        TypeErrorLabel.setVisible(true);
        LabTestTypeComboBox.requestFocus();
        flag = false;
    }
    if (LabTestResultsBox.getText().length() == 0)
    {
        LabTestResultsBox.setBackground(Color.PINK);
        LabTestResultsBox.requestFocus();
        flag = false;
    }
    if (LabTestDateTimeBox.getText().length() == 0)
    {
        LabTestDateTimeBox.setBackground(Color.PINK);
        LabTestDateTimeBox.requestFocus();
        flag = false;
    }
    if (flag == false)
        JOptionPane.showMessageDialog(this,"ONE OR MORE VALUES ARE INCORRECT","ENTRY ERROR",JOptionPane.ERROR_MESSAGE);
            
     return flag;
}
//Enters a new record into the Patient table if it passes validation
    private void DemoAddRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DemoAddRecordButtonActionPerformed
        //Call validation the insert upon success
        boolean flag;

        clearDemoValidation();
        flag = validateDemo();

        if (flag == true)
        {
            String insert="INSERT INTO patients(Medical_Number, Alt_Record_Number, Sex, Last_Name, First_Name, Middle, Address, City, State,"+
            "Zip_Code, Birth_Date, Home_Phone, Work_Phone, Email, Next_Of_Kin, Next_Of_Kin_Contact, General_Notes)VALUES('"+
            RecordNumBox.getText()+"', '"+AltRecordNumBox.getText()+"', '"+SexBox.getText()+"', '"+LastNameBox.getText()+"', '"+FirstNameBox.getText()+
            "', '"+MiddleInitBox.getText()+"', '"+AddressBox.getText()+"', '"+CityBox.getText()+"', '"+StateBox.getText()+"', '"+ZipBox.getText()
            +"', '"+BirthDayBox.getText()+"', '"+HomePhoneBox.getText()+"', '"+WorkPhoneBox.getText()+"', '"+EmailBox.getText()+"', '"+
            NextOfKinBox.getText()+"', '"+KinContactBox.getText()+"', '"+NotesBox.getText()+"')";

            try (Connection test=getConnection();
                Statement statement = test.createStatement())
            {
                int count = statement.executeUpdate(insert);
                if (count > 0 )
                JOptionPane.showMessageDialog(null, "Record inserted.");
                //Increase the max number of records by one then display the newly entered record
                
                MaxPatientRecords++;
                CurrentPatient=MaxPatientRecords;
                getPatient(CurrentPatient);
                DisplayPatient();
                
                PreviousButton.setEnabled(true);
                NextButton.setEnabled(true);
                DemoSearchBox.setEnabled(true);
                DemoSearchButton.setEnabled(true);
                DemoDelete.setEnabled(true);
                DisableDemoFields();
                ModifyDemoButton.setEnabled(true);

            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
        //
    }//GEN-LAST:event_DemoAddRecordButtonActionPerformed

   //Search for patients matching the patient id from the patient demographics panel
    private void DemoSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DemoSearchButtonActionPerformed
        // TODO add your handling code here:
        String Search=DemoSearchBox.getText();
        
        

        String SQL="SELECT * FROM PATIENTS WHERE Medical_Number LIKE '"+Search+"'";
        try (Connection test=getConnection();
            Statement statement = test.createStatement();
            ResultSet rs = statement.executeQuery(SQL);)
        {

            while(rs.next())
            {
                int ID=rs.getInt("Patient_ID");
                String num=rs.getString("Medical_Number");
                String alt=rs.getString("Alt_Record_Number");
                String Sex= rs.getString("Sex");
                String Last=rs.getString("Last_Name");
                String First=rs.getString("First_Name");
                String Mid=rs.getString("Middle");
                String Addy=rs.getString("Address");
                String City=rs.getString("City");
                String State=rs.getString("State");
                String Zip=rs.getString("Zip_Code");
                //Converts MYSQL Date into Gregorian Calendar format
                GregorianCalendar Date=new GregorianCalendar();
                Date.setTime(rs.getTimestamp("Birth_Date"));

                String HomePhone=rs.getString("Home_Phone");
                String WorkPhone=rs.getString("Work_Phone");
                String Email=rs.getString("Email");
                String Kin=rs.getString("Next_Of_Kin");
                String KinContact=rs.getString("Next_Of_Kin_Contact");
                String Notes=rs.getString("General_Notes");

                Incoming= new Patient(ID, num, alt, Sex, Last, First, Mid, Addy, City, State, Zip, Date, HomePhone, WorkPhone, Email, Kin, KinContact, Notes);
                
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        //Displays patient on patient panel
        ClearPatient();        
        DisplayPatient();
        
        /*int temp=PatientDemographics.getSelectedIndex();
        PatientDemographics.setSelectedIndex(-1);
        PatientDemographics.setSelectedIndex(temp);
        */

        //Gets encouters for patient and displays on encounters tab
        getEncounter();
        CurrentEncounter=0;
        ClearEncounters();
        DisplayEncounter(CurrentEncounter);
        
        //Gets vitals for patient and displays on vital signs tab
        getVitals();
        CurrentVital=0;
        ClearVitals();
        DisplayVital(CurrentVital);
        
        //Gets medications for patient and displays on medications tab
        getMedication();
        CurrentMedication=0;
        ClearMedication();
        DisplayMedication(CurrentMedication);
        
        //Gets labs for patient and displays on labs tab
        getLab();
        CurrentLab=0;
        ClearLabs();
        DisplayLab(CurrentLab);
    }//GEN-LAST:event_DemoSearchButtonActionPerformed

    //Allows for the user to press enter on the patient demographics search box instead of clicking button
    private void DemoSearchBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DemoSearchBoxActionPerformed

        DemoSearchButton.doClick();
    }//GEN-LAST:event_DemoSearchBoxActionPerformed

    //Displays the previous record for Patient Demographics Tab
    private void PreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreviousButtonActionPerformed

        if(CurrentPatient > 0)
        {
            CurrentPatient--;
            getPatient(CurrentPatient);
            DisplayPatient();
        }
    }//GEN-LAST:event_PreviousButtonActionPerformed

    //Displays the next record for Patient Demographics Tab
    private void NextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextButtonActionPerformed
        if(CurrentPatient < MaxPatientRecords)
        {
            CurrentPatient++;
            getPatient(CurrentPatient);
            DisplayPatient();
        }
    }//GEN-LAST:event_NextButtonActionPerformed

    //Clears patient demo form for new record entry
    private void CreateRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateRecordButtonActionPerformed
        //Gets the next value for the encounter id from the database
        DemoDelete.setEnabled(false);
        clearDemoValidation();
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
        PreviousButton.setEnabled(false);
        NextButton.setEnabled(false);
        ModifyDemoButton.setEnabled(false);
        DemoAddRecordButton.setEnabled(true);
        CreateRecordButton.setVisible(false);
        CancelAddButton.setVisible(true);
        CancelAddButton.setEnabled(true);
        
        int nextid=0;
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SHOW TABLE STATUS WHERE `Name` = 'patients'");)
        {    
            while(rs.next())
            {
                nextid=rs.getInt("Auto_Increment"); 
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        PatientIDBox.setText(String.valueOf(nextid));
        RecordNumBox.setText(null);
        AltRecordNumBox.setText(null);
        LastNameBox.setText(null);
        FirstNameBox.setText(null);
        MiddleInitBox.setText(null);
        AddressBox.setText(null);
        CityBox.setText(null);
        StateBox.setText(null);
        ZipBox.setText(null);
        BirthDayBox.setText(null);
        AgeBox.setText(null);
        HomePhoneBox.setText(null);
        WorkPhoneBox.setText(null);
        EmailBox.setText(null);
        NextOfKinBox.setText(null);
        KinContactBox.setText(null);
        NotesBox.setText(null);
        SexBox.setText(null);
        
        EnableDemoFields();
        FirstNameBox.requestFocus();
                                            
    }//GEN-LAST:event_CreateRecordButtonActionPerformed

    private void EnableDemoFields(){
        
        RecordNumBox.setEnabled(true);
        AltRecordNumBox.setEnabled(true);
        LastNameBox.setEnabled(true);
        FirstNameBox.setEnabled(true);
        MiddleInitBox.setEnabled(true);
        AddressBox.setEnabled(true);
        CityBox.setEnabled(true);
        StateBox.setEnabled(true);
        ZipBox.setEnabled(true);
        BirthDayBox.setEnabled(true);
        HomePhoneBox.setEnabled(true);
        WorkPhoneBox.setEnabled(true);
        EmailBox.setEnabled(true);
        NextOfKinBox.setEnabled(true);
        KinContactBox.setEnabled(true);
        NotesBox.setEnabled(true);
        SexBox.setEnabled(true);
    }
     private void EnableEncounterFields(){
        EncounterDateBox.setEnabled(true);
        EncounterTypeBox.setEnabled(true);
        EncounterDiagnosisBox.setEnabled(true);
        EncounterProviderBox.setEnabled(true);
        EncounterNotesBox.setEnabled(true);
     }
     private void EnableVitalFields(){
        VitalDateBox.setEnabled(true);
        VitalFootBox.setEnabled(true);
        VitalInchBox.setEnabled(true);
        VitalWeightBox.setEnabled(true);
        VitalHRBox.setEnabled(true);
        VitalSystolicBox.setEnabled(true);
        VitalDiastolicBox.setEnabled(true);
        VitalTempBox.setEnabled(true);
     }
     private void EnableLabFields(){
         LabTestTypeComboBox.setEnabled(true);
         LabTestDateTimeBox.setEnabled(true);
         LabTestDescriptionBox.setEnabled(true);
         LabTestResultsBox.setEnabled(true);
     }
     private void DisableLabFields(){
         LabTestTypeComboBox.setEnabled(false);
         LabTestDateTimeBox.setEnabled(false);
         LabTestDescriptionBox.setEnabled(false);
         LabTestResultsBox.setEnabled(false);
     }
    private void DisableDemoFields(){
        
        RecordNumBox.setEnabled(false);
        AltRecordNumBox.setEnabled(false);
        LastNameBox.setEnabled(false);
        FirstNameBox.setEnabled(false);
        MiddleInitBox.setEnabled(false);
        AddressBox.setEnabled(false);
        CityBox.setEnabled(false);
        StateBox.setEnabled(false);
        ZipBox.setEnabled(false);
        BirthDayBox.setEnabled(false);
        HomePhoneBox.setEnabled(false);
        WorkPhoneBox.setEnabled(false);
        EmailBox.setEnabled(false);
        NextOfKinBox.setEnabled(false);
        NotesBox.setEnabled(false);
        SexBox.setEnabled(false);
        KinContactBox.setEnabled(false);
        CancelAddButton.setVisible(false);
        CancelAddButton.setEnabled(false);
        CancelModifyDemoButton.setEnabled(false);
        CreateRecordButton.setVisible(true);
        SaveModifyButton.setEnabled(false);
        DemoAddRecordButton.setEnabled(false);
    }
    private void DisableEncounterFields(){
        EncounterDateBox.setEnabled(false);
        EncounterTypeBox.setEnabled(false);
        EncounterDiagnosisBox.setEnabled(false);
        EncounterProviderBox.setEnabled(false);
        EncounterNotesBox.setEnabled(false);
    }
    private void DisableVitalFields(){
        VitalDateBox.setEnabled(false);
        VitalFootBox.setEnabled(false);
        VitalInchBox.setEnabled(false);
        VitalWeightBox.setEnabled(false);
        VitalHRBox.setEnabled(false);
        VitalSystolicBox.setEnabled(false);
        VitalDiastolicBox.setEnabled(false);
        VitalTempBox.setEnabled(false);
    }
    private void DisableMedicationFields(){
        MedicationNameBox.setEnabled(false);
        DosageBox.setEnabled(false);
        DosageUnitsComboBox.setEnabled(false);
        DosageFrequencyComboBox.setEnabled(false);
        MedicationStartDateBox.setEnabled(false);
        MedicationEndDateBox.setEnabled(false);
        PrescribedByBox.setEnabled(false);
        InstructionsBox.setEnabled(false);
    }
    private void MedicationsPreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationsPreviousButtonActionPerformed
        // TODO add your handling code here:
        if(CurrentMedication > 0)
        {
            CurrentMedication--;
            DisplayMedication(CurrentMedication);
        }
    }//GEN-LAST:event_MedicationsPreviousButtonActionPerformed

    private void MedicationsNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationsNextButtonActionPerformed
        // TODO add your handling code here:
        if(CurrentMedication < MaxMedication)
        {
            CurrentMedication++;
            DisplayMedication(CurrentMedication);
        }
    }//GEN-LAST:event_MedicationsNextButtonActionPerformed

    private void EnableMedicationFields(){
        MedicationNameBox.setEnabled(true);
        DosageBox.setEnabled(true);
        DosageUnitsComboBox.setEnabled(true);
        DosageFrequencyComboBox.setEnabled(true);
        MedicationStartDateBox.setEnabled(true);
        MedicationEndDateBox.setEnabled(true);
        PrescribedByBox.setEnabled(true);        
        InstructionsBox.setEnabled(true);
    }
    //Clears the medications form for new record entry
    private void MedicationCreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationCreateButtonActionPerformed
        // TODO add your handling code here:
        //Gets the next value for the medication id from the database
        
        clearMedValidation();
        
        int nextid=0;
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SHOW TABLE STATUS WHERE `Name` = 'medications'");)
        {    
            while(rs.next())
            {
                nextid=rs.getInt("Auto_Increment"); 
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        MedicationDeleteRecordButton.setEnabled(false);
        EnableMedicationFields();
        MedicationsPreviousButton.setEnabled(false);
        MedicationsNextButton.setEnabled(false);
        MedicationCreateButton.setEnabled(false);
        MedicationAddRecordButton.setEnabled(true);
        MedicationCancelNewButton.setEnabled(true);
        MedicationModifyButton.setEnabled(false);
        MedicationSaveModifyButton.setEnabled(false);
        MedicationCancelModifyButton.setEnabled(false);
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
        MedicationDeleteRecordButton.setEnabled(false);
        MedicationIDBox.setText(String.valueOf(nextid));
        MedicationNameBox.setText(null);
        DosageBox.setText(null);
        DosageUnitsComboBox.setSelectedIndex(0);
        DosageFrequencyComboBox.setSelectedIndex(0);
        MedicationStartDateBox.setText(null);
        MedicationEndDateBox.setText(null);
        InstructionsBox.setText(null);
        PrescribedByBox.setText(null);
        CurrentPreviousMedicationList.setListData(new Object[0]);
        MedicationNameBox.requestFocus();
    }//GEN-LAST:event_MedicationCreateButtonActionPerformed
    
    //Enters a new record into the Medication table if it passes validation
    private void MedicationAddRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationAddRecordButtonActionPerformed
        // TODO add your handling code here:
        boolean flag;
        
        clearMedValidation();
        flag = validateMedication();
        
        if (flag == true)
        {
            
        String end=MedicationEndDateBox.getText();
        
        if(end.length()<5 || end==null)
        {
            end=null;
        }
        else
        {
            end="'"+MedicationEndDateBox.getText()+"'";
        }
        String insert="INSERT INTO medications (Patient_ID, Med_Name, Dosage, Dosage_Units, Dosage_Frequency, "+
                "Start_Date, End_Date, Instructions, Prescribed_By) VALUES("+Incoming.GetID()+", '"+MedicationNameBox.getText()+"',"
                +DosageBox.getText()+", '"+ DosageUnitsComboBox.getSelectedItem()+"', '"+DosageFrequencyComboBox.getSelectedItem()
                +"', '"+ MedicationStartDateBox.getText()+"', "+end+", '"
                +InstructionsBox.getText()+"', '"+PrescribedByBox.getText()+"')";
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(insert);
        
            if(count > 0)
            JOptionPane.showMessageDialog(null, "Record inserted.");
            clearMedValidation();
            resetMedication();
            getMedication();
            DisplayMedication(CurrentMedication);
            MedicationDeleteRecordButton.setEnabled(true);
                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        }
    }//GEN-LAST:event_MedicationAddRecordButtonActionPerformed

    private void MedicationDeleteRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationDeleteRecordButtonActionPerformed
        // TODO add your handling code here:
        int result=JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete this medication?",null,JOptionPane.YES_NO_OPTION);
        
        if(result==JOptionPane.YES_OPTION)
        {
          DeleteMedication();
        }
    }//GEN-LAST:event_MedicationDeleteRecordButtonActionPerformed

    private void LabPreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabPreviousButtonActionPerformed
        // TODO add your handling code here:
        if(CurrentLab > 0)
        {
            CurrentLab--;
            DisplayLab(CurrentLab);
        }
    }//GEN-LAST:event_LabPreviousButtonActionPerformed

    private void LabNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabNextButtonActionPerformed
        // TODO add your handling code here:
        if(CurrentLab < MaxLab)
        {
            CurrentLab++;
            DisplayLab(CurrentLab);
        }
    }//GEN-LAST:event_LabNextButtonActionPerformed

    //Clears the labs form for new record entry
    private void LabCreateRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabCreateRecordButtonActionPerformed
        // TODO add your handling code here:
        //Gets the next value for the lab id from the database
        
        clearLabValidation();        
        
        int nextid=0;
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SHOW TABLE STATUS WHERE `Name` = 'labs'");)
        {    
            while(rs.next())
            {
                nextid=rs.getInt("Auto_Increment"); 
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        EnableLabFields();
        LabPreviousButton.setEnabled(false);
        LabNextButton.setEnabled(false);
        LabCreateRecordButton.setEnabled(false);
        LabAddRecordButton.setEnabled(true);
        LabsCancelNewButton.setEnabled(true);
        LabModifyButton.setEnabled(false);
        LabSaveModifyButton.setEnabled(false);
        
        LabDeleteRecordButton.setEnabled(false);
        LabIDBox.setText(String.valueOf(nextid));
        LabTestTypeComboBox.setSelectedIndex(0);
        LabTestDescriptionBox.setText(null);
        LabTestResultsBox.setText(null);
        LabTestDateTimeBox.setText(null);
        AllLabsProceduresList.setListData(new Object[0]);
        
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
        LabTestTypeComboBox.requestFocus();
        
        
    }//GEN-LAST:event_LabCreateRecordButtonActionPerformed

    //Enters a new record into the Lab table if it passes validation
    private void LabAddRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabAddRecordButtonActionPerformed
    // TODO add your handling code here:
        boolean flag;
        
        clearLabValidation();
        flag = validateLab();
        
        if (flag == true)
        {
        String insert="INSERT INTO labs(Patient_ID,Test_Type,Test_Description,Test_Results,Test_Date_Time)VALUES("+Incoming.GetID()
                +", '"+LabTestTypeComboBox.getSelectedItem()+"', '"+LabTestDescriptionBox.getText()+"', '"
                + LabTestResultsBox.getText()+"', '"+LabTestDateTimeBox.getText()+"')";
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(insert);
        
            if(count > 0)
              JOptionPane.showMessageDialog(null, "Record inserted.");
            getLab();
            resetLab();
            DisplayLab(CurrentLab);
                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        }
    }//GEN-LAST:event_LabAddRecordButtonActionPerformed
    //Prompts user for input then deletes record from the labs table
    private void LabDeleteRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabDeleteRecordButtonActionPerformed
        int result=JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete this lab?",null,JOptionPane.YES_NO_OPTION);
        
        if(result==JOptionPane.YES_OPTION)
        {
           DeleteLab();
        }
        
    }//GEN-LAST:event_LabDeleteRecordButtonActionPerformed

    //Unlocks the controls for modification on the patient demo panel
    private void ModifyDemoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifyDemoButtonActionPerformed
        // TODO add your handling code here:
        EnableDemoFields();
        clearDemoValidation();
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
        PreviousButton.setEnabled(false);
        NextButton.setEnabled(false);
        CreateRecordButton.setEnabled(false);
        ModifyDemoButton.setVisible(false);
        CancelModifyDemoButton.setEnabled(true);
        SaveModifyButton.setVisible(true);
        SaveModifyButton.setEnabled(true);
        FirstNameBox.requestFocus(true);
        FirstNameBox.selectAll();
        DemoDelete.setEnabled(false);
        
    }//GEN-LAST:event_ModifyDemoButtonActionPerformed

    //Restores the existing data for the patient
    private void CancelAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelAddButtonActionPerformed
        // TODO add your handling code here:
        
        DisableDemoFields();
        DemoSearchBox.setEnabled(true);
        DemoSearchButton.setEnabled(true);
        PreviousButton.setEnabled(true);
        NextButton.setEnabled(true);
        ModifyDemoButton.setEnabled(true);
        CreateRecordButton.setEnabled(true);
        DemoAddRecordButton.setEnabled(false);  
        DemoDelete.setEnabled(true);
        
        DisplayPatient();
        
    }//GEN-LAST:event_CancelAddButtonActionPerformed

    //Restores the existing data for the patient
    private void CancelModifyDemoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelModifyDemoButtonActionPerformed
        // TODO add your handling code here:
        DisableDemoFields();
        DemoDelete.setEnabled(true);
        DemoSearchBox.setEnabled(true);
        DemoSearchButton.setEnabled(true);
        SaveModifyButton.setVisible(false);
        CancelModifyDemoButton.setEnabled(false);
        ModifyDemoButton.setVisible(true);
        PreviousButton.setEnabled(true);
        NextButton.setEnabled(true);
        CreateRecordButton.setEnabled(true);
        DisplayPatient();
    }//GEN-LAST:event_CancelModifyDemoButtonActionPerformed

    //Updates the patient table in the database with the modified record
    private void SaveModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveModifyButtonActionPerformed
        
        boolean flag;

        clearDemoValidation();
        flag = validateDemo();
              

        if (flag == true)
        {
        String insert="UPDATE patients SET Medical_Number ='"+ RecordNumBox.getText()+"', Alt_Record_Number ='"+AltRecordNumBox.getText()+"', Sex ='"+SexBox.getText()+
                "', Last_Name ='"+LastNameBox.getText()+"', First_Name ='"+FirstNameBox.getText()+"', Middle ='"+MiddleInitBox.getText()+"', Address ='"+AddressBox.getText()+
                "', City ='"+CityBox.getText()+"', State ='"+StateBox.getText()+"', Zip_Code ='"+ZipBox.getText()+"', Birth_Date ='"+BirthDayBox.getText()+
                "', Home_Phone ='"+HomePhoneBox.getText()+"', Work_Phone ='"+WorkPhoneBox.getText()+"', Email ='"+EmailBox.getText()+
                "', Next_Of_Kin ='"+NextOfKinBox.getText()+"', Next_Of_Kin_Contact ='"+KinContactBox.getText()+"', General_Notes ='"+NotesBox.getText()+
                "' WHERE Patient_ID ="+Incoming.GetID();

                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(insert);
        
            if(count > 0)
            {
             JOptionPane.showMessageDialog(null, "Record Updated.");
            
            DisableDemoFields();
            PreviousButton.setEnabled(true);
            NextButton.setEnabled(true);
            DemoAddRecordButton.setEnabled(false);
            CancelAddButton.setVisible(false);
            DemoSearchBox.setEnabled(true);
            DemoSearchButton.setEnabled(true);
            ModifyDemoButton.setVisible(true);
            ModifyDemoButton.setEnabled(true);
            CancelModifyDemoButton.setVisible(true);
            CancelModifyDemoButton.setEnabled(false);
            SaveModifyButton.setVisible(false);
            CreateRecordButton.setEnabled(true);
            getPatient(CurrentPatient);
            DisplayPatient();
            DemoDelete.setEnabled(true);
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        }
    }//GEN-LAST:event_SaveModifyButtonActionPerformed

    //reset encounter fields sub
    private void resetEncounter(){
        EncountersPreviousButton.setEnabled(true);               
        EncountersNextButton.setEnabled(true);                
        EncountersCreateButton.setEnabled(true);                
        EncounterAddRecordButton.setEnabled(false);                
        EncounterCancelAddButton.setEnabled(false);
        EncounterModifyCurrentButton.setEnabled(true);
        EncounterSaveModifyButton.setEnabled(false);
        EncounterCancelModifyButton.setEnabled(false);
        DemoSearchBox.setEnabled(true);
        DemoSearchButton.setEnabled(true);
    }
    //Enters a new record into the Encounter table if it passes validation
    private void EncounterAddRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncounterAddRecordButtonActionPerformed

        boolean flag;

        clearEncounterValidation();
        flag = validateEncounter();

        if (flag == true)
        {
            String insert="INSERT INTO encounters(Patient_ID,Encounter_Date,Encounter_Type,Notes,Diagnosis_Description,"+
            "Provider)VALUES("+Incoming.GetID()+", '"+EncounterDateBox.getText()+"','"+EncounterTypeBox.getSelectedItem()+"', '"+
            EncounterNotesBox.getText()+"', '"+EncounterDiagnosisBox.getText()+"', '"+ EncounterProviderBox.getText()+"')";

            try (Connection test=getConnection();
                Statement statement = test.createStatement())
            {
                int count = statement.executeUpdate(insert);

                if(count > 0)
                JOptionPane.showMessageDialog(null, "Record inserted.");
                getEncounter();
                DisplayEncounter(CurrentEncounter);
                DisableEncounterFields();
                resetEncounter();
                EncounterDelete.setEnabled(true);
            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }//GEN-LAST:event_EncounterAddRecordButtonActionPerformed

    //Selects the next Encounter from the array list
    private void EncountersNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncountersNextButtonActionPerformed
  
        if(CurrentEncounter < MaxEncounters)
        {
            CurrentEncounter++;
            DisplayEncounter(CurrentEncounter);
        }
    }//GEN-LAST:event_EncountersNextButtonActionPerformed

    
    private void EncountersPreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncountersPreviousButtonActionPerformed

        if(CurrentEncounter > 0)
        {
            CurrentEncounter--;
            DisplayEncounter(CurrentEncounter);
        }
    }//GEN-LAST:event_EncountersPreviousButtonActionPerformed

    
    private void EncountersCreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncountersCreateButtonActionPerformed

        clearEncounterValidation();
        
        int nextid=0;
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SHOW TABLE STATUS WHERE `Name` = 'encounters'");)
        {    
            while(rs.next())
            {
                nextid=rs.getInt("Auto_Increment"); 
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        EncounterDelete.setEnabled(false);
        EncounterIDBox.setText(String.valueOf(nextid));
        EncounterDateBox.setText(null);
        EncounterDiagnosisBox.setText(null);
        EncounterProviderBox.setText(null);
        EncounterNotesBox.setText(null);
        EncountersPreviousButton.setEnabled(false);
        EncountersNextButton.setEnabled(false);
        EncountersCreateButton.setEnabled(false);
        EncounterAddRecordButton.setEnabled(true);
        EncounterCancelAddButton.setEnabled(true);
        EncounterModifyCurrentButton.setEnabled(false);
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
        EnableEncounterFields();
        EncounterDateBox.requestFocus();
        
    }//GEN-LAST:event_EncountersCreateButtonActionPerformed

    
   
    //Saves the modifications to an encounter record
    private void EncounterSaveModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncounterSaveModifyButtonActionPerformed
        boolean flag;

        clearEncounterValidation();
        flag = validateEncounter();

        if (flag == true)
        {
            Encounters t=PatientEncounter.get(CurrentEncounter);
            
            String insert="UPDATE encounters SET Patient_ID="+Incoming.GetID()+", Encounter_Date='"+EncounterDateBox.getText()+"', Encounter_Type='"+
                EncounterTypeBox.getSelectedItem()+"',Notes='" +EncounterNotesBox.getText()+"', Diagnosis_Description='"+EncounterDiagnosisBox.getText()+
                "',Provider='"+ EncounterProviderBox.getText()+"'WHERE Encounter_ID="+t.GetID();

            try (Connection test=getConnection();
                Statement statement = test.createStatement())
            {
                int count = statement.executeUpdate(insert);

                if(count > 0)
                JOptionPane.showMessageDialog(null, "Record updated.");
                
                DisableEncounterFields();
                EncounterDelete.setEnabled(true);
                EncountersPreviousButton.setEnabled(true);
                EncountersNextButton.setEnabled(true);
                EncountersCreateButton.setEnabled(true);
                EncounterAddRecordButton.setEnabled(false);
                EncounterCancelAddButton.setEnabled(false);
                EncounterModifyCurrentButton.setEnabled(true);
                EncounterSaveModifyButton.setEnabled(false);
                EncounterCancelModifyButton.setEnabled(false);
                DemoSearchBox.setEnabled(true);
                DemoSearchButton.setEnabled(true);
                
                getEncounter();
                DisplayEncounter(CurrentEncounter);

            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }//GEN-LAST:event_EncounterSaveModifyButtonActionPerformed

    private void LabModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabModifyButtonActionPerformed
        clearLabValidation();
        EnableLabFields();
        LabTestTypeComboBox.requestFocus();
        LabPreviousButton.setEnabled(false);
        LabNextButton.setEnabled(false);
        LabCreateRecordButton.setEnabled(false);
        LabAddRecordButton.setEnabled(false);
        LabsCancelNewButton.setEnabled(false);
        LabModifyButton.setEnabled(false);
        LabSaveModifyButton.setEnabled(true);
        LabCancelModificationButton.setEnabled(true);
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
    }//GEN-LAST:event_LabModifyButtonActionPerformed

    private void EncounterCancelAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncounterCancelAddButtonActionPerformed
        // TODO add your handling code here:
        DisableEncounterFields();
        try{
        DisplayEncounter(CurrentEncounter);
        }
        catch(Exception e)
        {
            
        }
        resetEncounter();
        EncounterDelete.setEnabled(true);
    }//GEN-LAST:event_EncounterCancelAddButtonActionPerformed

    private void EncounterModifyCurrentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncounterModifyCurrentButtonActionPerformed
        // TODO add your handling code here:
        clearEncounterValidation();
        EncounterDelete.setEnabled(false);
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
        EncountersPreviousButton.setEnabled(false);
        EncountersNextButton.setEnabled(false);
        EncountersCreateButton.setEnabled(false);
        EncounterAddRecordButton.setEnabled(false);
        EncounterCancelAddButton.setEnabled(false);
        EncounterModifyCurrentButton.setEnabled(false);
        EncounterSaveModifyButton.setEnabled(true);
        EncounterCancelModifyButton.setEnabled(true);
        EnableEncounterFields();
        EncounterDateBox.requestFocus();
        EncounterDateBox.selectAll();
    }//GEN-LAST:event_EncounterModifyCurrentButtonActionPerformed

    private void EncounterCancelModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncounterCancelModifyButtonActionPerformed
        // TODO add your handling code here:
        DisableEncounterFields();
        try{
        DisplayEncounter(CurrentEncounter);
        }
        catch(Exception e)
        {
            
        }    
        resetEncounter();
        
        EncounterDelete.setEnabled(true);
    }//GEN-LAST:event_EncounterCancelModifyButtonActionPerformed

    private void VitalNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalNewButtonActionPerformed
        
        clearVitalValidation();
        
        int nextid=0;
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(
                "SHOW TABLE STATUS WHERE `Name` = 'vitalsigns'");)
        {    
            while(rs.next())
            {
                nextid=rs.getInt("Auto_Increment"); 
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        Date d = new Date();
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        VitalDelete.setEnabled(false);
        VitalIDBox.setText(String.valueOf(nextid));
        VitalDateBox.setText(output.format(d));
        VitalFootBox.setText(null);
        VitalInchBox.setText(null);
        VitalWeightBox.setText(null);
        VitalHRBox.setText(null);
        VitalSystolicBox.setText(null);
        VitalDiastolicBox.setText(null);
        VitalTempBox.setText(null);
        VitalNextButton.setEnabled(false);
        VitalPreviousButton.setEnabled(false);
        VitalNewButton.setEnabled(false);
        VitalSaveNewButton.setEnabled(true);
        VitalCancelNewButton.setEnabled(true);
        VitalModifyCurrentButton.setEnabled(false);
        VitalSaveModifyButton.setEnabled(false);
        VitalCancelModifyButton.setEnabled(false);
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
        EnableVitalFields();
        SysLabel.setText(null);
        DiaLabel.setText(null);
        VitalSystolicBox.setBackground(Color.white);
        VitalDiastolicBox.setBackground(Color.white);
        
        // run list box query
        VitalListBox.removeAll();
        //Selects the previous vitals into an array list
        int tempid=Incoming.GetID();
        String CurrentMed="SELECT * FROM Vitalsigns WHERE Patient_ID="+tempid;
        ArrayList<String> preVital=new ArrayList<>();
        
        
        
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(CurrentMed);)
        {    
            while(rs.next())
            {
                preVital.add("Vital_Date_Time:"+rs.getString("Date")+" Height:"+rs.getString("Height")+"  Weight:"+rs.getString("Weight")+"  BP:"+rs.getString("BloodPressure")+"  Temp:"+rs.getString("Temperature"));
                
                

            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        //Converts the arraylist into an array and displays it in the listbox
        VitalListBox.setListData(preVital.toArray());
        VitalFootBox.requestFocus();
    }//GEN-LAST:event_VitalNewButtonActionPerformed
//checks BP for warning messaage
    private void VitalSystolicBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_VitalSystolicBoxFocusLost
        // TODO add your handling code here:
    int s;
    try
    {
        s = Integer.parseInt(VitalSystolicBox.getText());
         if (s <= 0)
        {
            VitalSystolicBox.setBackground(Color.white);
            DiaLabel.setText("");
        }
        if (s <= 120)
        {
            VitalSystolicBox.setBackground(Color.green);
            SysLabel.setText("Normal");
        }
        if (120 < s && s < 140)
        {
            VitalSystolicBox.setBackground(Color.yellow);
            SysLabel.setText("Elevated");
        }
        if (140 < s && s < 160)
        {
            VitalSystolicBox.setBackground(Color.orange);
            SysLabel.setText("Caution");
        }
        if (160 < s)
        {
            VitalSystolicBox.setBackground(Color.red);
            SysLabel.setText("WARNING!");
        }
    }
        catch(NumberFormatException e){
            }
    }//GEN-LAST:event_VitalSystolicBoxFocusLost
//part two
    private void VitalDiastolicBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_VitalDiastolicBoxFocusLost
        // TODO add your handling code here:
        int d;
    try
    {
        d = Integer.parseInt(VitalDiastolicBox.getText());
        
        if (d <= 0)
        {
            VitalDiastolicBox.setBackground(Color.white);
            DiaLabel.setText("");
        }
        if (d <= 80)
        {
            VitalDiastolicBox.setBackground(Color.green);
            DiaLabel.setText("Normal");
        }
        if (81 < d && d < 90)
        {
            VitalDiastolicBox.setBackground(Color.yellow);
            DiaLabel.setText("Elevated");
        }
        if (90 < d && d < 105)
        {
            VitalDiastolicBox.setBackground(Color.orange);
            DiaLabel.setText("Caution");
        }
        if (105 < d)
        {
            VitalDiastolicBox.setBackground(Color.red);
            DiaLabel.setText("WARNING!");
        }
    }
        catch(NumberFormatException e){
            }
    }//GEN-LAST:event_VitalDiastolicBoxFocusLost

    private void VitalSaveNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalSaveNewButtonActionPerformed

       boolean flag;

        clearVitalValidation();
        flag = validateVital();

        if (flag == true)
            {
            String insert="INSERT INTO vitalsigns(Patient_ID,Height,Weight, HeartRate, BloodPressure,Temperature,Vital_Date_Time)"
                    + "VALUES("+Incoming.GetID()+", '"+(VitalFootBox.getText()+"-"+VitalInchBox.getText())+"', "+VitalWeightBox.getText()+", '"+VitalHRBox.getText()+
                    "', '"+(VitalSystolicBox.getText()+"/"+VitalDiastolicBox.getText())+"', "+VitalTempBox.getText()+", '"+ VitalDateBox.getText()+"')";
            
            try (Connection test=getConnection();
                Statement statement = test.createStatement())
            {
                int count = statement.executeUpdate(insert);

                if(count > 0)
                JOptionPane.showMessageDialog(null, "Record inserted.");
                getVitals();
                DisplayVital(CurrentVital);                
                DisableVitalFields();
                resetVital();
                VitalDelete.setEnabled(true);
                 }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }//GEN-LAST:event_VitalSaveNewButtonActionPerformed

    private void VitalCancelNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalCancelNewButtonActionPerformed
        // TODO add your handling code here:
        clearVitalValidation();
        DisableVitalFields();
        try{
        DisplayVital(CurrentVital);
        }
        catch(Exception e)
        {
            
        }
        resetVital();
        VitalDelete.setEnabled(true);
    }//GEN-LAST:event_VitalCancelNewButtonActionPerformed
//vitals previous button
    private void VitalPreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalPreviousButtonActionPerformed

        if(CurrentVital > 0)
        {
            CurrentVital--;
            DisplayVital(CurrentVital);
        }                        
        SysLabel.setText(null);
        DiaLabel.setText(null);
        VitalSystolicBox.setBackground(Color.white);
        VitalDiastolicBox.setBackground(Color.white);
    }//GEN-LAST:event_VitalPreviousButtonActionPerformed
//vitals next button
    private void VitalNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalNextButtonActionPerformed
         if(CurrentVital < MaxVital)
        {
            CurrentVital++;
            DisplayVital(CurrentVital);
        }
        SysLabel.setText(null);
        DiaLabel.setText(null);
        VitalSystolicBox.setBackground(Color.white);
        VitalDiastolicBox.setBackground(Color.white);
    }//GEN-LAST:event_VitalNextButtonActionPerformed

    private void VitalModifyCurrentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalModifyCurrentButtonActionPerformed
        // TODO add your handling code here:
        
        EnableVitalFields();
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
        VitalPreviousButton.setEnabled(false);
        VitalNextButton.setEnabled(false);
        VitalNewButton.setEnabled(false);
        VitalModifyCurrentButton.setEnabled(false);
        VitalSaveModifyButton.setEnabled(true);
        VitalCancelModifyButton.setEnabled(true);
        SysLabel.setText(null);
        DiaLabel.setText(null);
        VitalSystolicBox.setBackground(Color.white);
        VitalDiastolicBox.setBackground(Color.white);
        VitalFootBox.requestFocus();
        VitalFootBox.selectAll();
        VitalDelete.setEnabled(false);
    }//GEN-LAST:event_VitalModifyCurrentButtonActionPerformed

    private void AllLabsProceduresButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllLabsProceduresButtonActionPerformed

        //Selects all labs into an array list
        int tempid=Incoming.GetID();
        String Labs="SELECT Test_Type FROM LABS WHERE Patient_ID="+tempid;
        ArrayList<String> nextlab=new ArrayList<>();
        
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(Labs);)
        {    
            while(rs.next())
            {
                nextlab.add(rs.getString("Test_Type"));
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        //Converts the arraylist into an array and displays it in the listbox
    AllLabsProceduresList.setListData(nextlab.toArray());
    }//GEN-LAST:event_AllLabsProceduresButtonActionPerformed
    
    //Inserts the patients into the listbox on the reports tab
    private void Reports()
    {
        String Labs="SELECT Patient_ID, Last_Name, First_Name FROM Patients ORDER BY Patient_ID";
        ArrayList<String> patients=new ArrayList<>();
        
        ReportPatientBox.removeAllItems();
        
        try (Connection test=getConnection();
             Statement statement = test.createStatement();
                ResultSet rs = statement.executeQuery(Labs);)
        {    
            while(rs.next())
            {
              ReportPatientBox.addItem(rs.getString("Patient_ID")+" "+rs.getString("Last_Name")+ ", "+rs.getString("First_Name"));
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
    }
    //Creates a report for the currently selected patient
    private void ReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReportButtonActionPerformed
       
        
        CurrentPatient=ReportPatientBox.getSelectedIndex();
        getPatient(CurrentPatient);
        getEncounter();
        getLab();
        getMedication();
        getVitals();
        
        Path file=Paths.get("D:\\Users\\Team5CNIT350\\Desktop\\"+Incoming.GetLast()+"_"+Incoming.GetFirst()+".txt");
        String print=" ";
        
        try
        {
        OutputStream output=new BufferedOutputStream(Files.newOutputStream(file));
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(output));
        
        //Prints the vitals of the selected patient
        print= "Patient ID: "+ Incoming.GetID() + "  Medical Record Number: " + Incoming.GetRecord () + "  Alt Medical Record Number: " + Incoming.GetAltRecord();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print = " ";
        writer.write(print,0,print.length());
        writer.newLine();
         
        print=Incoming.GetFirst() + " " + Incoming.GetMiddle() +" "+ Incoming.GetLast();
        writer.write(print,0,print.length());
        writer.newLine();
         
        print=Incoming.GetAddress();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print=Incoming.GetCity() + "," + Incoming.GetState() + " " + Incoming.GetZipCode();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print = " ";
        writer.write(print,0,print.length());
        writer.newLine();
         
        print="Sex: "+Incoming.GetSex() + "  Age: "+Incoming.GetAge()+ "  DOB: "+Incoming.GetBirthDate();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print = " ";
        writer.write(print,0,print.length());
        writer.newLine();
         
        print="H: "+Incoming.GetHomePhone() + "  W: "+ Incoming.GetWorkPhone() + "  Email: "+Incoming.GetEmail() ;
        writer.write(print,0,print.length());
        writer.newLine();
        
        print = " ";
        writer.write(print,0,print.length());
        writer.newLine();
         
        print="EMERGENCY CONTACT: "+Incoming.GetNextOfKin() + " - "+Incoming.GetNextOfKinContact();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print = " ";
        writer.write(print,0,print.length());
        writer.newLine();
         
        print="Notes: "+Incoming.GetNotes();
        writer.write(print,0,print.length());
        writer.newLine();
                    
        writer.newLine();
        writer.newLine();
        print="Encounters: ";
        writer.write(print,0,print.length());
        writer.newLine();
        
        
        //Loops through all the encounters for the patient and prints each one
        for(int count=0; count<=MaxEncounters; count++)
        {
        Encounters t=PatientEncounter.get(count);
        
        writer.newLine();
        print="Encounter : "+String.valueOf(count+1);
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Encounter Date: "+t.GetDate();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Encounter Type: "+t.GetType();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Diagnosis Description: "+t.GetDescription();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Provider: "+t.GetProvider();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Notes: "+t.GetNotes();
        writer.write(print,0,print.length());
        writer.newLine();           
            
        }
        
        writer.newLine();
        writer.newLine();
        print="Vital Signs: ";
        writer.write(print,0,print.length());
        writer.newLine();
        //Loops through all the vital signs for the patient and prints each one
        for(int count=0; count<=MaxVital; count++)
        {
        VitalSigns t=PatientVital.get(count);
        
        writer.newLine();
        print="Vitals : "+String.valueOf(count+1);
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Height: "+t.GetHeight();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Weight: "+t.GetWeight();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Heart Rate: "+t.GetHeart();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Blood Pressure: "+t.GetBloodPressure();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Temperature: "+t.GetTemperature();
        writer.write(print,0,print.length());
        writer.newLine();
           
        }
        
        
        writer.newLine();
        writer.newLine();
        print="Medications: ";
        writer.write(print,0,print.length());
        writer.newLine();
        //Loops through all the medications for the patient and prints each one
        for(int count=0; count<=MaxMedication; count++)
        {
        Medications t=PatientMedication.get(count);
        
        writer.newLine();
        print="Medication : "+String.valueOf(count+1);
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Medication Name: "+t.GetName();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Dosage: "+t.GetDosage();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Dosage Units: "+t.GetDosageUnits();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Dosage Frequency: "+t.GetDosageFrequency();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Start Date: "+t.GetStartDate();
        writer.write(print,0,print.length());
        writer.newLine(); 
        
        print="End Date: "+t.GetEndDate();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Instructions: "+t.GetInstructions();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Prescribed By: "+t.GetPrescribedBy();
        writer.write(print,0,print.length());
        writer.newLine();
            
        }
        
        
        writer.newLine();
        writer.newLine();
        print="Labs: ";
        writer.write(print,0,print.length());
        writer.newLine();
        //Loops through all the Labs for the patient and prints each one
        for(int count=0; count<=MaxLab; count++)
        {
        Labs t=PatientLab.get(count);
        
        writer.newLine();
        print="Lab : "+String.valueOf(count+1);
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Test Type: "+t.GetType();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Test Description: "+t.GetDescription();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Test Results: "+t.GetResults();
        writer.write(print,0,print.length());
        writer.newLine();
        
        print="Test Date: "+t.GetDate();
        writer.write(print,0,print.length());
        writer.newLine();
           
        }
        //Closes the file
        writer.close();
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_ReportButtonActionPerformed
//Deletes a patient from the database
    private void DeletePatient()
    {        
        //Remove patient records from the encounters table
        String delete="DELETE FROM Encounters WHERE Patient_ID="+Incoming.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {            
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Remove patient records from the vitalsigns table
        delete="DELETE FROM Vitalsigns WHERE Patient_ID="+Incoming.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {                
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Remove patient records from the medications table
        delete="DELETE FROM medications WHERE Patient_ID="+Incoming.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {                
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Remove patient records from the labs table
        delete="DELETE FROM Labs WHERE Patient_ID="+Incoming.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {                             
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
        //Remove patient record from the patient table
        delete="DELETE FROM Patients WHERE Patient_ID="+Incoming.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(delete);
        
            if(count > 0)
              JOptionPane.showMessageDialog(null, "Record Deleted.");
            ClearPatient();
            ClearEncounters();
            ClearVitals();
            ClearMedication();
            ClearLabs();
            CurrentPatient=0;
            getPatient(CurrentPatient);
            DisplayPatient();                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
    }
    
    //Deletes an encounter from the database
    private void DeleteEncounter()
    {
         Encounters t=PatientEncounter.get(CurrentEncounter);
        String delete="DELETE FROM Encounters WHERE Encounter_ID="+t.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(delete);
        
            if(count > 0)
              JOptionPane.showMessageDialog(null, "Record Deleted.");
            getEncounter();
            ClearEncounters();
            DisplayEncounter(CurrentEncounter);
                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        
    }
    
    //Deletes a vital from the database
    private void DeleteVital()
    {
         VitalSigns t=PatientVital.get(CurrentVital);
           String delete="DELETE FROM Vitalsigns WHERE Vital_Sign_ID="+t.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(delete);
        
            if(count > 0)
              JOptionPane.showMessageDialog(null, "Record Deleted.");
            getVitals();
            ClearVitals();
            DisplayVital(CurrentVital);
                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
    }
    
    //Deletes a medication from the database
    private void DeleteMedication()
    {
      Medications t=PatientMedication.get(CurrentMedication);
           String delete="DELETE FROM medications WHERE Medication_ID="+t.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(delete);
        
            if(count > 0)
              JOptionPane.showMessageDialog(null, "Record Deleted.");
            getMedication();
            ClearMedication();
            DisplayMedication(CurrentMedication);
                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }   
    }
    
    //Deletes a lab from the database
    private void DeleteLab()
    {
        Labs t=PatientLab.get(CurrentLab);
           String delete="DELETE FROM Labs WHERE Lab_ID="+t.GetID();
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(delete);
        
            if(count > 0)
              JOptionPane.showMessageDialog(null, "Record Deleted.");
            getLab();
            ClearLabs();
            DisplayLab(CurrentLab);
                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        } 
    }
    private void resetVital(){
        VitalPreviousButton.setEnabled(true);
        VitalNextButton.setEnabled(true);
        VitalNewButton.setEnabled(true);
        VitalSaveNewButton.setEnabled(false);
        VitalCancelNewButton.setEnabled(false);
        VitalModifyCurrentButton.setEnabled(true);
        VitalSaveModifyButton.setEnabled(false);
        VitalCancelModifyButton.setEnabled(false);
        DemoSearchBox.setEnabled(true);
        DemoSearchButton.setEnabled(true);
        SysLabel.setText(null);
        DiaLabel.setText(null);
        VitalDiastolicBox.setBackground(Color.white);
        VitalSystolicBox.setBackground(Color.white);
    }
    
    private void resetMedication(){
        DisableMedicationFields();
        clearMedValidation();
        MedicationsPreviousButton.setEnabled(true);
        MedicationsNextButton.setEnabled(true);
        MedicationCreateButton.setEnabled(true);
        MedicationAddRecordButton.setEnabled(false);
        MedicationCancelNewButton.setEnabled(false);
        MedicationModifyButton.setEnabled(true);
        MedicationSaveModifyButton.setEnabled(false);
        MedicationCancelModifyButton.setEnabled(false);
        UnitErrorLabel.setVisible(false);
        FreqErrorLabel.setVisible(false);
        DemoSearchBox.setEnabled(true);
        DemoSearchButton.setEnabled(true);
        MedicationDeleteRecordButton.setEnabled(true);
        DisplayMedication(CurrentMedication);
    }
    
    private void resetLab(){
        clearLabValidation();
        DisableLabFields();
        LabPreviousButton.setEnabled(true);
        LabNextButton.setEnabled(true);
        LabCreateRecordButton.setEnabled(true);
        LabAddRecordButton.setEnabled(false);
        LabsCancelNewButton.setEnabled(false);
        LabModifyButton.setEnabled(true);
        LabSaveModifyButton.setEnabled(false);
        LabCancelModificationButton.setEnabled(false);
        TypeErrorLabel.setVisible(false);
        DemoSearchBox.setEnabled(true);
        DemoSearchButton.setEnabled(true);
        LabDeleteRecordButton.setEnabled(true);
        DisplayLab(CurrentLab);
    }
    
    private void VitalCancelModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalCancelModifyButtonActionPerformed

        clearVitalValidation();
        DisableVitalFields();
        try{
        DisplayVital(CurrentVital);
        }
        catch(Exception e)
        {
            
        }
        resetVital();
        VitalDelete.setEnabled(true);
    }//GEN-LAST:event_VitalCancelModifyButtonActionPerformed

    private void VitalSaveModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalSaveModifyButtonActionPerformed

        boolean flag;

        clearVitalValidation();
        flag = validateVital();

        if (flag == true)
        {
            VitalSigns t=PatientVital.get(CurrentVital);
             String update="UPDATE vitalsigns SET Height='"+(VitalFootBox.getText()+"-"+VitalInchBox.getText())+"', Weight="+VitalWeightBox.getText()+
                     ",HeartRate='"+VitalHRBox.getText()+"', BloodPressure='"+(VitalSystolicBox.getText()+"/"+VitalDiastolicBox.getText())+"',Temperature="+VitalTempBox.getText()+
                     ",Vital_Date_Time='"+ VitalDateBox.getText()+"'WHERE Patient_ID ="+Incoming.GetID()+" AND Vital_Sign_ID="+t.GetID();
            
             try (Connection test=getConnection();
                Statement statement = test.createStatement())
            {
                int count = statement.executeUpdate(update);

                if(count > 0)
                JOptionPane.showMessageDialog(null, "Record inserted.");
                getVitals();
                DisplayVital(CurrentVital);
                DisableVitalFields();
                resetVital();
                VitalDelete.setEnabled(true);
                 }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }//GEN-LAST:event_VitalSaveModifyButtonActionPerformed

    //Displays the current patient's medications
    private void CurrentMedicationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CurrentMedicationButtonActionPerformed

        //switch buttons
        CurrentMedicationButton.setVisible(false);
        PreviousMedicationButton.setVisible(true);
        
        //Selects the current medications into an array list
        int tempid=Incoming.GetID();
        String CurrentMed="SELECT Med_Name FROM MEDICATIONS WHERE Patient_ID="+tempid +" AND END_DATE IS NULL";
        ArrayList<String> nextmed=new ArrayList<>();

        CurrentPreviousMedicationList.removeAll();

        try (Connection test=getConnection();
            Statement statement = test.createStatement();
            ResultSet rs = statement.executeQuery(CurrentMed);)
        {
            while(rs.next())
            {
                nextmed.add(rs.getString("Med_Name"));
                

            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        //Converts the arraylist into an array and displays it in the listbox
        CurrentPreviousMedicationList.setListData(nextmed.toArray());
    }//GEN-LAST:event_CurrentMedicationButtonActionPerformed

    private void PreviousMedicationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreviousMedicationButtonActionPerformed

        PreviousMedicationButton.setVisible(false);
        CurrentMedicationButton.setVisible(true);
        
        //Selects the previous medications into an array list
        int tempid=Incoming.GetID();
        String CurrentMed="SELECT Med_Name FROM MEDICATIONS WHERE Patient_ID="+tempid +" AND END_DATE IS NOT NULL";
        ArrayList<String> nextmed=new ArrayList<>();
        
        CurrentPreviousMedicationList.removeAll();
        

        try (Connection test=getConnection();
            Statement statement = test.createStatement();
            ResultSet rs = statement.executeQuery(CurrentMed);)
        {
            while(rs.next())
            {
                nextmed.add(rs.getString("Med_Name"));
                

            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        //Converts the arraylist into an array and displays it in the listbox
        CurrentPreviousMedicationList.setListData(nextmed.toArray());
        
        
    }//GEN-LAST:event_PreviousMedicationButtonActionPerformed

    private void MedicationCancelNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationCancelNewButtonActionPerformed
        // TODO add your handling code here:
        resetMedication();
        try{    
        DisplayMedication(CurrentMedication);    
        }
        catch(Exception e)
        {
            
        }
        MedicationDeleteRecordButton.setEnabled(true);
    }//GEN-LAST:event_MedicationCancelNewButtonActionPerformed

    private void VitalDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VitalDeleteActionPerformed
        // TODO add your handling code here:
        int result=JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete this vital record?",null,JOptionPane.YES_NO_OPTION);
        
        if(result==JOptionPane.YES_OPTION)
        {
          DeleteVital();
        }
    }//GEN-LAST:event_VitalDeleteActionPerformed

    private void EncounterDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncounterDeleteActionPerformed
        // TODO add your handling code here:
        int result=JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete this encounter?",null,JOptionPane.YES_NO_OPTION);
        
        if(result==JOptionPane.YES_OPTION)
        {
          DeleteEncounter();
        }
    }//GEN-LAST:event_EncounterDeleteActionPerformed

    private void DemoDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DemoDeleteActionPerformed
        // TODO add your handling code here:
        int result=JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete this patient? This will permanently remove ALL of the patient's associated records. These records are not recoverable.",null,JOptionPane.YES_NO_OPTION);
        
        if(result==JOptionPane.YES_OPTION)
        {
          DeletePatient();
        }
    }//GEN-LAST:event_DemoDeleteActionPerformed

    private void MedicationSaveModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationSaveModifyButtonActionPerformed
        boolean flag;
        
        clearMedValidation();
        flag = validateMedication();
        
        if (flag == true)
        {
            
        String end=MedicationEndDateBox.getText();
        
        if(end.length()<5 || end==null)
        {
            end=null;
        }
        else
        {
            end="'"+MedicationEndDateBox.getText()+"'";
        }
        
        Medications t=PatientMedication.get(CurrentMedication);
        String insert="UPDATE medications SET Med_Name='"+MedicationNameBox.getText()+"', Dosage="+DosageBox.getText()+
                ", Dosage_Units='"+ DosageUnitsComboBox.getSelectedItem()+"', Dosage_Frequency='"+DosageFrequencyComboBox.getSelectedItem()+
                "', Start_Date='"+ MedicationStartDateBox.getText()+"', End_Date="+end+", Instructions='"+
                InstructionsBox.getText()+"', Prescribed_By='"+PrescribedByBox.getText()+"' WHERE Medication_ID="+t.GetID();
                
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(insert);
        
            if(count > 0)
              JOptionPane.showMessageDialog(null, "Record updated.");
            getMedication();
            DisplayMedication(CurrentMedication);
            clearMedValidation();
            resetMedication();
            MedicationDeleteRecordButton.setEnabled(true);
                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        }
    }//GEN-LAST:event_MedicationSaveModifyButtonActionPerformed

    private void LabsCancelNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabsCancelNewButtonActionPerformed
         // TODO add your handling code here:
        resetLab();
        try{
        DisplayLab(CurrentLab);   
        }
        catch(Exception e)
        {
            
        }
        LabDeleteRecordButton.setEnabled(true);
        
    }//GEN-LAST:event_LabsCancelNewButtonActionPerformed

    private void MedicationCancelModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationCancelModifyButtonActionPerformed
        // TODO add your handling code here:
        MedicationDeleteRecordButton.setEnabled(true);
        DemoSearchBox.setEnabled(true);
        DemoSearchButton.setEnabled(true);
        clearMedValidation();
        resetMedication();
        try{
         DisplayMedication(CurrentMedication);   
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_MedicationCancelModifyButtonActionPerformed

    private void MedicationModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MedicationModifyButtonActionPerformed
        clearMedValidation();
        EnableMedicationFields();
        MedicationNameBox.requestFocus();
        MedicationNameBox.selectAll();
        MedicationsPreviousButton.setEnabled(false);
        MedicationsNextButton.setEnabled(false);
        MedicationCreateButton.setEnabled(false);
        MedicationAddRecordButton.setEnabled(false);
        MedicationCancelNewButton.setEnabled(false);
        MedicationModifyButton.setEnabled(false);
        MedicationSaveModifyButton.setEnabled(true);
        MedicationCancelModifyButton.setEnabled(true);
        DemoSearchBox.setEnabled(false);
        DemoSearchButton.setEnabled(false);
    }//GEN-LAST:event_MedicationModifyButtonActionPerformed

    private void LabSaveModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabSaveModifyButtonActionPerformed
        // TODO add your handling code here:
        boolean flag;
        
        clearLabValidation();
        flag = validateLab();
        
        if (flag == true)
        {       
        
        Labs t=PatientLab.get(CurrentLab);
        String insert="UPDATE Labs SET Test_Type='"+LabTestTypeComboBox.getSelectedItem()+"', Test_Description='"+LabTestDescriptionBox.getText()+
                "', Test_Results='"+ LabTestResultsBox.getText()+"', Test_Date_Time='"+LabTestDateTimeBox.getText()+"' WHERE Lab_ID="+t.GetID();
             
                
        try (Connection test=getConnection();
             Statement statement = test.createStatement())
        {        
            int count = statement.executeUpdate(insert);
        
            if(count > 0)
              JOptionPane.showMessageDialog(null, "Record updated.");
            getLab();
            DisplayLab(CurrentLab);
            ClearLabs();
            resetLab();
            LabDeleteRecordButton.setEnabled(true);
                       
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
        }
    }//GEN-LAST:event_LabSaveModifyButtonActionPerformed

    private void LabCancelModificationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabCancelModificationButtonActionPerformed
        LabDeleteRecordButton.setEnabled(true);
        DemoSearchBox.setEnabled(true);
        DemoSearchButton.setEnabled(true);
        clearLabValidation();
        resetLab();
        try{
        DisplayLab(CurrentLab);   
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_LabCancelModificationButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PatientDemographics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PatientDemographics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PatientDemographics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PatientDemographics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() 
            {
                //Default Code:
                /*
                new PatientDemographics().setVisible(true);
                */
                
                
                //Create and Center On Screen
                PatientDemographics test=new PatientDemographics();
                test.setLocationRelativeTo(null);
                test.setVisible(true);
            }
        });
    }
    //Clears the patient panel
    private void ClearPatient()
    {
    GlobalNameLabel.setText(null);
    GlobalAgeLabel.setText(null);
    GlobalSexLabel.setText(null);
    PatientIDBox.setText(null);
    RecordNumBox.setText(null);
    AltRecordNumBox.setText(null);
    LastNameBox.setText(null);
    FirstNameBox.setText(null);
    MiddleInitBox.setText(null);
    AddressBox.setText(null);
    CityBox.setText(null);
    StateBox.setText(null);
    ZipBox.setText(null);
    BirthDayBox.setText(null);
    AgeBox.setText(null);
    HomePhoneBox.setText(null);
    WorkPhoneBox.setText(null);
    EmailBox.setText(null);
    NextOfKinBox.setText(null);
    KinContactBox.setText(null);
    NotesBox.setText(null);
    SexBox.setText(null); 
    }
    
    //Clears the encounters panel
    private void ClearEncounters()
    {
    EncounterIDBox.setText(null);
    EncounterDateBox.setText(null);
    EncounterTypeBox.setSelectedIndex(-1);
    EncounterNotesBox.setText(null);
    EncounterDiagnosisBox.setText(null);
    EncounterProviderBox.setText(null);
    }
    
    //Clears the vitals panel
    private void ClearVitals()
    {
    VitalIDBox.setText(null);
    VitalFootBox.setText(null);
    VitalInchBox.setText(null);
    VitalWeightBox.setText(null); 
    VitalHRBox.setText(null);
    VitalSystolicBox.setText(null);
    VitalDiastolicBox.setText(null);    
    VitalTempBox.setText(null);
    VitalDateBox.setText(null);    
    VitalListBox.removeAll();   
    }
    
    //Clears the medications panel
    private void ClearMedication()
    {
    MedicationIDBox.setText(null);
    MedicationNameBox.setText(null);
    DosageBox.setText(null);
    DosageUnitsComboBox.setSelectedIndex(-1);
    DosageFrequencyComboBox.setSelectedIndex(-1);
    MedicationStartDateBox.setText(null);
    MedicationEndDateBox.setText(null);
    InstructionsBox.setText(null);
    PrescribedByBox.setText(null);
    CurrentPreviousMedicationList.removeAll();
           
    }
    
    //Clears the labs panel
    private void ClearLabs()
    {
    LabIDBox.setText(null);
    LabTestTypeComboBox.setSelectedIndex(-1);
    LabTestDescriptionBox.setText(null);
    LabTestResultsBox.setText(null);
    LabTestDateTimeBox.setText(null);
    AllLabsProceduresList.removeAll();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AddressBox;
    private javax.swing.JTextField AgeBox;
    private javax.swing.JButton AllLabsProceduresButton;
    private javax.swing.JList AllLabsProceduresList;
    private javax.swing.JTextField AltRecordNumBox;
    private javax.swing.JTextField BirthDayBox;
    private javax.swing.JButton CancelAddButton;
    private javax.swing.JButton CancelModifyDemoButton;
    private javax.swing.JTextField CityBox;
    private javax.swing.JButton CreateRecordButton;
    private javax.swing.JButton CurrentMedicationButton;
    private javax.swing.JList CurrentPreviousMedicationList;
    private javax.swing.JButton DemoAddRecordButton;
    private javax.swing.JButton DemoDelete;
    private javax.swing.JTextField DemoSearchBox;
    private javax.swing.JButton DemoSearchButton;
    private javax.swing.JLabel DiaLabel;
    private javax.swing.JTextField DosageBox;
    private javax.swing.JComboBox DosageFrequencyComboBox;
    private javax.swing.JComboBox DosageUnitsComboBox;
    private javax.swing.JTextField EmailBox;
    private javax.swing.JButton EncounterAddRecordButton;
    private javax.swing.JButton EncounterCancelAddButton;
    private javax.swing.JButton EncounterCancelModifyButton;
    private javax.swing.JTextField EncounterDateBox;
    private javax.swing.JButton EncounterDelete;
    private javax.swing.JTextField EncounterDiagnosisBox;
    private javax.swing.JLabel EncounterErrorLabel;
    private javax.swing.JTextField EncounterIDBox;
    private javax.swing.JButton EncounterModifyCurrentButton;
    private javax.swing.JTextArea EncounterNotesBox;
    private javax.swing.JTextField EncounterProviderBox;
    private javax.swing.JButton EncounterSaveModifyButton;
    private javax.swing.JComboBox EncounterTypeBox;
    private javax.swing.JButton EncountersCreateButton;
    private javax.swing.JButton EncountersNextButton;
    private javax.swing.JPanel EncountersPanel;
    private javax.swing.JButton EncountersPreviousButton;
    private javax.swing.JTextField FirstNameBox;
    private javax.swing.JLabel FreqErrorLabel;
    private javax.swing.JLabel GlobalAgeLabel;
    private javax.swing.JLabel GlobalNameLabel;
    private javax.swing.JLabel GlobalSexLabel;
    private javax.swing.JTextField HomePhoneBox;
    private javax.swing.JTextArea InstructionsBox;
    private javax.swing.JTextField KinContactBox;
    private javax.swing.JButton LabAddRecordButton;
    private javax.swing.JButton LabCancelModificationButton;
    private javax.swing.JButton LabCreateRecordButton;
    private javax.swing.JButton LabDeleteRecordButton;
    private javax.swing.JTextField LabIDBox;
    private javax.swing.JButton LabModifyButton;
    private javax.swing.JButton LabNextButton;
    private javax.swing.JButton LabPreviousButton;
    private javax.swing.JButton LabSaveModifyButton;
    private javax.swing.JTextField LabTestDateTimeBox;
    private javax.swing.JTextArea LabTestDescriptionBox;
    private javax.swing.JTextArea LabTestResultsBox;
    private javax.swing.JComboBox LabTestTypeComboBox;
    private javax.swing.JButton LabsCancelNewButton;
    private javax.swing.JPanel LabsProceduresPanel;
    private javax.swing.JTextField LastNameBox;
    private javax.swing.JButton MedicationAddRecordButton;
    private javax.swing.JButton MedicationCancelModifyButton;
    private javax.swing.JButton MedicationCancelNewButton;
    private javax.swing.JButton MedicationCreateButton;
    private javax.swing.JButton MedicationDeleteRecordButton;
    private javax.swing.JTextField MedicationEndDateBox;
    private javax.swing.JTextField MedicationIDBox;
    private javax.swing.JButton MedicationModifyButton;
    private javax.swing.JTextField MedicationNameBox;
    private javax.swing.JButton MedicationSaveModifyButton;
    private javax.swing.JTextField MedicationStartDateBox;
    private javax.swing.JButton MedicationsNextButton;
    private javax.swing.JPanel MedicationsPanel;
    private javax.swing.JButton MedicationsPreviousButton;
    private javax.swing.JTextField MiddleInitBox;
    private javax.swing.JButton ModifyDemoButton;
    private javax.swing.JButton NextButton;
    private javax.swing.JTextField NextOfKinBox;
    private javax.swing.JTextArea NotesBox;
    private javax.swing.JTabbedPane PatientDemographics;
    private javax.swing.JTextField PatientIDBox;
    private javax.swing.JPanel PatientsDemoPanel;
    private javax.swing.JTextField PrescribedByBox;
    private javax.swing.JButton PreviousButton;
    private javax.swing.JButton PreviousMedicationButton;
    private javax.swing.JTextField RecordNumBox;
    private javax.swing.JButton ReportButton;
    private javax.swing.JComboBox ReportPatientBox;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JButton SaveModifyButton;
    private javax.swing.JLabel Search;
    private javax.swing.JTextField SexBox;
    private javax.swing.JTextField StateBox;
    private javax.swing.JLabel SysLabel;
    private javax.swing.JLabel TypeErrorLabel;
    private javax.swing.JLabel UnitErrorLabel;
    private javax.swing.JButton VitalCancelModifyButton;
    private javax.swing.JButton VitalCancelNewButton;
    private javax.swing.JTextField VitalDateBox;
    private javax.swing.JButton VitalDelete;
    private javax.swing.JTextField VitalDiastolicBox;
    private javax.swing.JTextField VitalFootBox;
    private javax.swing.JTextField VitalHRBox;
    private javax.swing.JTextField VitalIDBox;
    private javax.swing.JTextField VitalInchBox;
    private javax.swing.JList VitalListBox;
    private javax.swing.JButton VitalModifyCurrentButton;
    private javax.swing.JButton VitalNewButton;
    private javax.swing.JButton VitalNextButton;
    private javax.swing.JButton VitalPreviousButton;
    private javax.swing.JButton VitalSaveModifyButton;
    private javax.swing.JButton VitalSaveNewButton;
    private javax.swing.JPanel VitalSignsPanel;
    private javax.swing.JTextField VitalSystolicBox;
    private javax.swing.JTextField VitalTempBox;
    private javax.swing.JTextField VitalWeightBox;
    private javax.swing.JTextField WorkPhoneBox;
    private javax.swing.JTextField ZipBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}

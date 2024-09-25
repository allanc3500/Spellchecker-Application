/***************************************************************
Student Name: Allan Chung
File Name: SystemInfo.java Given by Default. Gets Java Properties
Assignment number: Project 2

***************************************************************/
package uwf.project2;

public class SystemInfo {

    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

}
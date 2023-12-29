import java.util.Date;



public class clientConfigFile {

    //conversion constants
    final long HOURS_TO_SECONDS = 3600;
    final long MINUTES_TO_SECONDS = 60;

    //Standard update interval and date
    public int updateInterval = 30;
    public String dateWithTime = "";

    clientConfigFile(String s){

        if(s == null){
            return;
        }

        formatConfigAndInitializeParameters(s);

    }

    public void formatConfigAndInitializeParameters(String rawConfig) {

        if(rawConfig == null){
            return;
        }

        String[] temp;
        temp = rawConfig.split(";");

        if(temp.length == 1){
            updateInterval = Integer.parseInt(temp[0]);
            return;
        }

        updateInterval = Integer.parseInt(temp[0]);
        dateWithTime = temp[1];



    }

    //checks if the current time is over the config time on the server
    public Boolean isTimeOver(){

        if(dateWithTime.isEmpty())
            return false;

        //reformatting the date to match java date formatting
        String[] temp = dateWithTime.split(" ");
        String[] date = temp[0].split("[.]");
        String[] time = temp[1].split(":");

        long currentTimeInSeconds = new Date().getTime()/1000;
        Date configDate = new Date(Integer.parseInt(date[2]) - 1900,Integer.parseInt(date[1]) - 1 ,Integer.parseInt(date[0])); //stupid shit
        long configDateInSeconds = configDate.getTime()/1000;
        configDateInSeconds += Integer.parseInt(time[0])*HOURS_TO_SECONDS + Integer.parseInt(time[1])*MINUTES_TO_SECONDS + Integer.parseInt(time[2]);

        return currentTimeInSeconds > configDateInSeconds;


    }

}

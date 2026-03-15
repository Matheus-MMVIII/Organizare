import java.util.Calendar;

public class Users {
    private int id;
    private String name;
    private String email;
    private String cellPhone;
    private int birthMonth;
    private int birthDay;

    public Users(int id, String name, String email, String cellPhone, int birthMonth, int birthDay) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cellPhone = cellPhone;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public int[] getDateOfBirth() {
        return new int[] { birthMonth, birthDay };
    }

    public boolean isBirthdayToday() {
        Calendar today = Calendar.getInstance();
        int todayMonth = today.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        return todayMonth == birthMonth && todayDay == birthDay;
    }

}
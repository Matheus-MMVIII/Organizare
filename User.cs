using System;

public class User
{
    public int id { get; set; }
    public string? name { get; set; }
    public string? number { get; set; }
    public DateTime birthDate { get; set; }
    public string? email { get; set; }

    public int GetAge()
    {
        DateTime today = DateTime.Today;

        int age = today.Year - birthDate.Year;

        if (birthDate.Date > today.AddYears(-age))
            age--;

        return age;
    }

    public bool IsBirthdayToday()
    {
        DateTime today = DateTime.Today;

        return birthDate.Day == today.Day &&
               birthDate.Month == today.Month;
    }
}
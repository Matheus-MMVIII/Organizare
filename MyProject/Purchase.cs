using System;

public class Purchase
{
    public int id { get; set; }
    public int userId { get; set; }
    public string? item { get; set; }
    public decimal price { get; set; }
    public DateTime purchaseDate { get; set; }
}
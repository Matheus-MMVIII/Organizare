using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.IO;


public static class PurchaseRecord
{
    private static List<Purchase> purchases = new();
    private static readonly string path =
        Path.Combine(
            Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData),
            "MyApp",
            "purchases.json"
        );//Vai para C:\Users\User\AppData\Roaming\MyApp

    public static Purchase Add(int userId, string item, decimal price)
    {
        int newId = purchases.Count > 0 ? purchases.Max(p => p.id) + 1 : 1;
        Purchase purchase = new Purchase
        {
            id = newId,
            userId = userId,
            item = item,
            price = price,
            purchaseDate = DateTime.Now
        };
        purchases.Add(purchase);
        Save();
        return purchase;
    }

    public static List<Purchase> GetByUserId(int userId)
    {
        return purchases.Where(p => p.userId == userId).ToList();
    }

    public static List<Purchase> GetAll()
    {
        return purchases.ToList();
    }

    public static void Save()
    {
        var dir = Path.GetDirectoryName(path);
        if (!string.IsNullOrEmpty(dir) && !Directory.Exists(dir))
        {
            Directory.CreateDirectory(dir);
        }
    
        string json = JsonSerializer.Serialize(purchases, new JsonSerializerOptions { WriteIndented = true });

        File.WriteAllText(path, json, Encoding.UTF8);
    }

    public static void Load()
    {
        Console.WriteLine(path);
        if (!File.Exists(path))
        {
            purchases = new List<Purchase>();
            return;
        }

        string json = File.ReadAllText(path, Encoding.UTF8);

        purchases = JsonSerializer.Deserialize<List<Purchase>>(json) ?? new List<Purchase>();
    }

}
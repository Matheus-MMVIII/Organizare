using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


public static class PurchaseRecord
{
    private static List<Purchase> purchases = new List<Purchase>();
    private static string path =
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
        return purchases;
    }

    public static void Save()
    {
        string json = JsonSerializer.Serialize(purchases);
        Directory.CreateDirectory(Path.GetDirectoryName(path));
        File.WriteAllText(path, json);
    }

}
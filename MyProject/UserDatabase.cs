using System;
using System.IO;
using System.Text;
using System.Text.Json;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Globalization;

public static class UserDatabase
{
    private static List<User> users = new List<User>();
    private static readonly string path =
        Path.Combine(
            Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData),
            "MyApp",
            "users.json"
        );//Vai para C:\Users\User\AppData\Roaming\MyApp

    private static readonly byte[] key = Encoding.UTF8.GetBytes("96174894108961748941089617494109"); //subistituir por env se for escolhido para criptrografar
    private static readonly byte[] iv = Encoding.UTF8.GetBytes("1746860020068409");

    public static User Add(string name, string number, DateTime birthDate, string email)
    {
        int newId = users.Count > 0 ? users.Max(u => u.id) + 1 : 1;

        User user = new User
        {
            id = newId,
            name = name,
            number = number,
            birthDate = birthDate,
            email = email,
            isActive = true
        };

        users.Add(user);
        Save();

        return user;
    }

    public static bool Remove(int id)
    {
        var user = users.FirstOrDefault(u => u.id == id);
        if (user == null) return false;

        user.isActive = false;
        Save();
        return true;
    }

    public static bool Delete(int id)
    {
        var user = users.FirstOrDefault(u => u.id == id);
        if (user == null) return false;

        users.Remove(user);
        Save();
        return true;
    }

    public static bool Update(int id, string name, DateTime birthDate, string email)
    {
        var user = users.FirstOrDefault(u => u.id == id);
        if (user == null) return false;

        user.name = name;
        user.birthDate = birthDate;
        user.email = email;

        Save();
        return true;
    }

    public static void CheckBirthdays()
    {
        foreach (var user in users)
        {
            if (user.IsBirthdayToday())
            {
                //algo pra quando for o dia do aniversario do usuario
            }
        }
    }

    public static User? FindByEmail(string email)
    {
        return users.FirstOrDefault(u => u.email == email);
    }

    public static User? FindByName(string name)
    {
        return users.FirstOrDefault(u => u.name == name);
    }

    public static User? FindByNumner(string number)
    {
        return users.FirstOrDefault(u => u.number == number);
    }

    public static User? FindById(int id)
    {
        return users.FirstOrDefault(u => u.id == id);
    }

    public static List<User> GetAll()
    {
        return new List<User>(users);
    }

    private static void Save()// A um limite de usuario para ser salvo, por isso, se for necessário salvar muitos usuarios, é recomendado usar um banco de dados ou dividir os arquivos em partes menores.
    {
        Directory.CreateDirectory(Path.GetDirectoryName(path)!);

        string json = JsonSerializer.Serialize(users, new JsonSerializerOptions { WriteIndented = true });

        //string encrypted = Encrypt(json);

        File.WriteAllText(path, json, Encoding.UTF8);// Com o modo que estou usando, o arquivo é salvo sem criptografia. Para usar a criptografia, substitua 'json' por 'encrypted' e descomente as linhas relacionadas à criptografia.
    }

    private static void Load()
    {
        if (!File.Exists(path)) return;

        string json = File.ReadAllText(path, Encoding.UTF8);

        //Sistem Encrypte and Descrypt
        //string encrypted = File.ReadAllText(path, Encoding.UTF8);
        //string json = Decrypt(encrypted);

        users = JsonSerializer.Deserialize<List<User>>(json)?? new List<User>();
    }


    private static string Encrypt(string text)
    {
        using Aes aes = Aes.Create();
        aes.Key = key;
        aes.IV = iv;

        using var encryptor = aes.CreateEncryptor();
        byte[] inputBytes = Encoding.UTF8.GetBytes(text);
        byte[] encrypted = encryptor.TransformFinalBlock(inputBytes, 0, inputBytes.Length);

        return Convert.ToBase64String(encrypted);
    }

    private static string Decrypt(string encryptedText)
    {
        using Aes aes = Aes.Create();
        aes.Key = key;
        aes.IV = iv;

        using var decryptor = aes.CreateDecryptor();
        byte[] buffer = Convert.FromBase64String(encryptedText);
        byte[] decrypted = decryptor.TransformFinalBlock(buffer, 0, buffer.Length);

        return Encoding.UTF8.GetString(decrypted);
    }
}
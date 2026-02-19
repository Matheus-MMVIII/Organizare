using System;
using System.Timers;
using System.Reflection.PortableExecutable;

public class Program
{
    static void Main()
    {

        // ADD
        UserDatabase.Add(
            "Matheus",
            "(62) 9 8536-8815",
            new DateTime(2008, 2, 14),
            "matheus@email.com"
        );

        UserDatabase.Add(
            "Lorena",
            "(62) 9 9297-2020",
            new DateTime(1987, 7, 10),
            "lorena@email.com"
        );

        PurchaseRecord.Add(1, "Roupa P", 89.90m);

        //Console.WriteLine("Criado ID: " + u1.id);

        // FIND
        //var found = UserDatabase.FindByEmail("lorena@email.com");
        //if (found != null)
        //    Console.WriteLine("Encontrado: " + found.name);

        // UPDATE
        //UserDatabase.Update(u1.id, "Matheus V2", new DateTime(2008, 2, 14), "novo@email.com");

        // REMOVE
        //UserDatabase.Remove(u2.id);

        // LIST
        //foreach (var u in UserDatabase.GetAll())
        //{
        //    Console.WriteLine($"{u.id} - {u.name}");
        //}

        //UserDatabase.CheckBirthdays();
    }

}
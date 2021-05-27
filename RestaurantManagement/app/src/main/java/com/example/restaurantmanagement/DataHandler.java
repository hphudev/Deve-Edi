package com.example.restaurantmanagement;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DataHandler
{
    private DatabaseReference databaseReference;

    public void Read(String parentNodeName, String ID)
    {
        GetChildReference(parentNodeName, ID).addValueEventListener(new ValueEventListener()
        {
            @Override public void onDataChange(DataSnapshot dataSnapshot)
            {
                switch (parentNodeName)
                {
                    case "user":
                    {
                        User user = dataSnapshot.getValue(User.class);
                        break;
                    }
                    case "bill":
                    {
                        Bill bill = dataSnapshot.getValue(Bill.class);
                        break;
                    }
                    case "food":
                    {
                        Food food = dataSnapshot.getValue(Food.class);
                        break;
                    }
                    case "detail bill":
                    {
                        DetailBill detailBill = dataSnapshot.getValue(DetailBill.class);
                        break;
                    }
                }
            }

            @Override public void onCancelled(DatabaseError error)
            {

            }
        });
    }

    public void Delete(String parentNodeName, String ID)
    {
        Insert(parentNodeName, ID, null);
    }

    public void Update(String parentNodeName, String ID, IdentifiableObject value)
    {
        Insert(parentNodeName, ID, value);
    }

    DatabaseReference GetChildReference(String parentNodeName, String ID)
    {
        this.databaseReference = FirebaseDatabase.getInstance().getReference(parentNodeName);

        return databaseReference.child(ID);
    }

    public void Insert(String parentNodeName, String ID, IdentifiableObject value)
    {
        GetChildReference(parentNodeName, ID).setValue(value);
    }
}
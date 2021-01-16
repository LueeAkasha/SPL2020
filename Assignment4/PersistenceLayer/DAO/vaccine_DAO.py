# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 13:27:16 2021

@author: luee
"""
from PersistenceLayer.DTO.vaccine_DTO import vaccine_DTO

class _vaccine_DAO:
    def __init__(self, conn):
        self._conn = conn  
        pass
    
    def insert(self, vaccine):
        try:
            self._conn.execute("""INSERT INTO vaccines (date, supplier, quantity) VALUES (?, ?, ?);""", [vaccine.get_date(), vaccine.get_supplier(), vaccine.get_quantity()])
            self._conn.commit()
        except Exception as error:
            print(error)
        pass
    
    def delete(self, _id):
        try:
            self._conn.execute(""" DELETE FROM vaccines where id=? ;""",[_id])
            self._conn.commit()
        except Exception as error:
            print(error)
            
    def update_amount_and_process(self, vaccine, amount_to_reduce):
        remainder = None
        try:
            update = """ UPDATE vaccines SET quantity=? where id=? ;"""
            delete = """ DELETE FROM vaccines WHERE id=? ;"""
            if vaccine.get_quantity() == amount_to_reduce:
                self._conn.execute(delete, [vaccine.get_id()])
                remainder = 0
            elif vaccine.get_quantity() > amount_to_reduce:
                self._conn.execute(update, [vaccine.get_quantity() - amount_to_reduce, vaccine.get_id()])
                remainder = 0                
            elif vaccine.get_quantity() < amount_to_reduce:
                self._conn.execute(delete, [vaccine.get_id()])
                remainder = amount_to_reduce - vaccine.get_quantity()
            self._conn.commit()
        except Exception as error:
            print(error)
        return remainder      

      
    def get_older_vaccine(self):
        vaccine = None
        try:
            cursor = self._conn.cursor()
            vac_row = cursor.execute("""
            SELECT *
            FROM vaccines
            ORDER BY vaccines.date ASC
            LIMIT 1;                          
            """).fetchone()
            vaccine = vaccine_DTO(vac_row[0], vac_row[1], vac_row[2], vac_row[3])
            self._conn.commit()
            cursor.close()
        except Exception as error:
            print(error)
        return vaccine


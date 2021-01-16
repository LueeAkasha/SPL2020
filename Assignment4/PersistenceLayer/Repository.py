# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 15:14:17 2021

@author: luee
"""
import sqlite3
import atexit
from datetime import datetime
from PersistenceLayer.DAO.vaccine_DAO import _vaccine_DAO
from PersistenceLayer.DAO.supplier_DAO import _supplier_DAO
from PersistenceLayer.DAO.clinic_DAO import _clinic_DAO
from PersistenceLayer.DAO.logistic_DAO import _logistic_DAO
from PersistenceLayer.DTO.vaccine_DTO import vaccine_DTO
from PersistenceLayer.DTO.supplier_DTO import supplier_DTO
from PersistenceLayer.DTO.clinic_DTO import clinic_DTO
from PersistenceLayer.DTO.logistic_DTO import logistic_DTO

class _Repository:    
    def __init__(self):
        self._conn = sqlite3.connect('database.db')
        self.v_DAO = _vaccine_DAO(self._conn)
        self.s_DAO = _supplier_DAO(self._conn)
        self.c_DAO = _clinic_DAO(self._conn)
        self.l_DAO = _logistic_DAO(self._conn)
        pass
    
    def get_connection(self):
        return self._conn
    
    def _close(self):
        self._conn.commit()
        self._conn.close()
    
    def create_tables(self):
        try:
            self._conn.executescript("""        
            CREATE TABLE logistics (
                id                 INT     PRIMARY KEY,
                name     TEXT    NOT NULL,
                count_sent  INT     NOT NULL,
                count_received  INT  NOT NULL
            );
            
            CREATE TABLE suppliers (
                id                 INT     PRIMARY KEY,
                name     TEXT    NOT NULL UNIQUE,
                logistic   INT     NOT NULL,
                FOREIGN KEY(logistic)     REFERENCES logistics(id)
            );
     
            CREATE TABLE clinics (
                id      INT     PRIMARY KEY,
                location  TEXT     NOT NULL UNIQUE,
                demand           INT     NOT NULL,
                logistic   INT     NOT NULL,
                FOREIGN KEY(logistic)     REFERENCES logistics(id)
            );
            
            CREATE TABLE vaccines (
                id      INTEGER,
                date    DATE        NOT NULL,
                supplier    INT,
                quantity    INT     NOT NULL,
                FOREIGN KEY(supplier)     REFERENCES suppliers(id),
                PRIMARY KEY("id" AUTOINCREMENT)
            );
            """)
            self._conn.commit()
        except Exception as error:
            print(error) 
            pass
        pass
    
    def store(self, database_records):
        indicator = database_records.pop(0)
        for i in range(len(indicator)):
            indicator[i] = int(indicator[i].split('\n')[0])
        number_of_vaccines = indicator[0]
        number_of_suppliers = indicator[1]
        number_of_clinics = indicator[2]
        number_of_logistics = indicator[3]
        
        while(number_of_vaccines > 0):
            record = database_records.pop(0)
            # we do not consider the id from the config file.......
            vaccine = vaccine_DTO(None, datetime.strptime(record[1].replace('-','−'), '%Y−%m−%d').date(), int(record[2]), int(record[3].split('\n')[0]))
            self.v_DAO.insert(vaccine)
            number_of_vaccines-=1
            pass
        while(number_of_suppliers > 0):
            record = database_records.pop(0)
            supplier = supplier_DTO(int(record[0]), record[1], int(record[2].split('\n')[0]))
            self.s_DAO.insert(supplier)
            number_of_suppliers-=1
        while(number_of_clinics > 0):
            record = database_records.pop(0)
            clinic = clinic_DTO(int(record[0]), record[1], int(record[2]), int(record[3].split('\n')[0]))
            self.c_DAO.insert(clinic)
            number_of_clinics-=1
        while(number_of_logistics > 0):
            record = database_records.pop(0)
            logistic = logistic_DTO(int(record[0]), record[1], int(record[2]), int(record[3].split('\n')[0]))
            self.l_DAO.insert(logistic)
            number_of_logistics-=1
        pass
    
    def get_total_demand(self):
        output = None
        try:
            cursor = self._conn.cursor()
            output = cursor.execute("""SELECT sum(demand)
            FROM clinics;""").fetchone()[0]
            cursor.close()
        except Exception as error:
            print(error)
        return output
    
    def get_total_inventory(self):
        output = None
        try:
            cursor = self._conn.cursor()
            output = cursor.execute(""" SELECT sum(quantity)
            FROM vaccines; """).fetchone()[0]
            cursor.close()
        except Exception as error:
            print(error)
        return output        
    
    def get_total_sent(self):
        output = None
        try:
            cursor = self._conn.cursor()
            output = cursor.execute("""SELECT sum(count_sent) FROM logistics;""").fetchone()[0]
            cursor.close()
        except Exception as error:
            print(error)
        return output
    
    def get_total_received(self):
        output = None
        try:
            cursor = self._conn.cursor()
            output = cursor.execute("""SELECT sum(count_received) FROM logistics;""").fetchone()[0]
            cursor.close()
        except Exception as error:
            print(error)
        return output    
    
    def send_shipment(self, location, amount):
        try:
            temp = amount
            self.c_DAO.update_demand(location,amount)
            while(amount >0):
                vaccine = self.v_DAO.get_older_vaccine()
                temp = amount
                amount = self.v_DAO.update_amount_and_process(vaccine, amount)
                clinic = self.c_DAO.get_clinic_by_location(location)
                logistic = clinic.get_logistic()
                self.l_DAO.update_count_sent(logistic, temp-amount)
            
            with open("output.txt", "a") as file_object:
                output_str =str(self.get_total_inventory())+','+str(self.get_total_demand())+","+str(self.get_total_received())+","+str(self.get_total_sent())+"\n"
                file_object.write(output_str)
        except Exception as error:
            print(error)
       
    def receive_shipment(self, name, amount, date):
        try:
            
            supplier = self.s_DAO.get_supplier_by_name(name)
            vaccine = vaccine_DTO(None, date, supplier.get_id(), amount)
            self.v_DAO.insert(vaccine)
            logistic = supplier.get_logistic()
            self.l_DAO.update_count_received(logistic, amount)
            with open("output.txt", "a") as file_object:
                output_str =str(self.get_total_inventory())+','+str(self.get_total_demand())+","+str(self.get_total_received())+","+str(self.get_total_sent())+"\n"
                file_object.write(output_str)
        except Exception as error:
            print(error)
        

repo = _Repository()
atexit.register(repo._close)

# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""
import sys
from datetime import datetime
from PersistenceLayer.Repository import repo

def database_building(database_records_as_strings):
    database_records = list()
    for i in range(len(database_records_as_strings)):
        database_records.append(database_records_as_strings[i].split(','))
    repo.create_tables()
    repo.store(database_records)
    

def manage_orders(orders):
    for i in range(len(orders)):
        record = orders[i].split(',')
        if len(record) == 2:
            repo.send_shipment(record[0], int(record[1]))
        elif len(record) == 3:
            repo.receive_shipment(record[0], int(record[1]), datetime.strptime(record[2].split('\n')[0].replace('-','−'), '%Y−%m−%d').date())

def main(config_file, orders_file):
    with open(config_file, "r") as cfile:
        lines = cfile.readlines()
        database_building(lines)
    with open(orders_file, "r") as ofile:
        lines = ofile.readlines()
        manage_orders(lines)

if __name__ == '__main__':
    main(sys.argv[1], sys.argv[2])

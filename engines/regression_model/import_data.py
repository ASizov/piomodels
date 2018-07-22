# -*- coding: utf-8 -*-

"""
Import sample data for classification engine
"""

import predictionio
import argparse
import os
import sys
import pandas as pd

def import_events(client, path, group):
  df = pd.read_csv(path)
  if args.group_variables is not None:
    df = df.groupby(by = group, as_index = False).sum()
  count = 0
  print("Importing data...")
  for data in df.to_dict('records'):
    client.create_event(event="$set",
                        entity_type="user",
                        entity_id=str(count), # use the count num as user ID
                        properties=data)
    count += 1
  print("%s events are imported." % count)

def download_datafile(url, path):
  if sys.version_info[0] >= 3:
    from urllib.request import urlretrieve
  else:
    from urllib import urlretrieve
  urlretrieve(url, path)

if __name__ == '__main__':
  parser = argparse.ArgumentParser(
    description="Import sample data")
  parser.add_argument('--access-key', dest='access_key', default='BarcelAppI34s1fa9DXja')
  parser.add_argument('--url', default="http://localhost:7070")
  parser.add_argument('--file', dest='file_path', default=None)
  #parser.add_argument('--download-url', dest='download_url', default=None)
  parser.add_argument('--group-by', dest='group_variables', default=None)

  args = parser.parse_args()
  print(args)

  if args.file_path is None:
    download_datafile(args.download_url, args.file_path)
  elif not os.path.exists(args.file_path):
    download_datafile(args.download_url, args.file_path)
  else:
    pass  

  client = predictionio.EventClient(
    access_key=args.access_key,
    url=args.url,
    threads=5,
    qsize=500)
  import_events(client, args.file_path, args.group_variables)
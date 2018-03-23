# Kouchlin
WIP: A Kotlin client for CouchDB

## API Progress

### Server

| Endpoint                                     | Method | Status                   |
|----------------------------------------------|--------|--------------------------|
| /                                            | GET    | :heavy_check_mark:       |
| /_all_dbs                                    | GET    | :heavy_check_mark:       |
| /_db_updates                                 | GET    | :heavy_check_mark:       |
| /_active_tasks                               | GET    | :hourglass_flowing_sand: |
| /_replicate                                  | POST   | :hourglass_flowing_sand: |
| /_uuids                                      | GET    | :hourglass_flowing_sand: |
| /_cluster_setup                              | GET    | :heavy_multiplication_x: |
| /_cluster_setup                              | POST   | :heavy_multiplication_x: |
| /_membership                                 | GET    | :heavy_multiplication_x: |
| /_scheduler/jobs                             | GET    | :heavy_multiplication_x: |
| /_scheduler/docs                             | GET    | :heavy_multiplication_x: |
| /_scheduler/docs/{replicator_db}             | GET    | :heavy_multiplication_x: |
| /_scheduler/docs/{replicator_db}/{docid}     | GET    | :heavy_multiplication_x: |
| /_restart                                    | POST   | :heavy_multiplication_x: |
| /_stats                                      | GET    | :heavy_multiplication_x: |

### Database

| Endpoint                                     | Method | Status                   |
|----------------------------------------------|--------|--------------------------|
| /db                                          | HEAD   | :heavy_check_mark:       |
| /db                                          | GET    | :heavy_check_mark:       |
| /db                                          | PUT    | :heavy_check_mark:       |
| /db                                          | DELETE | :heavy_check_mark:       |
| /db/_compact                                 | POST   | :heavy_check_mark:       |
| /db/_ensure_full_commit                      | POST   | :heavy_check_mark:       |
| /db/_all_docs                                | GET    | :heavy_check_mark:       |
| /db/_all_docs                                | POST   | :hourglass_flowing_sand: |
| /db/_bulk_docs                               | POST   | :heavy_check_mark:       |
| /db/_changes                                 | GET    | :heavy_check_mark:       |
| /db/_changes                                 | POST   | :hourglass_flowing_sand: |


#### Find

| Endpoint                                     | Method | Status                   |
|----------------------------------------------|--------|--------------------------|
| /db/_find                                    | POST   | :hourglass_flowing_sand: |
| /db/_explain                                 | POST   | :hourglass_flowing_sand: |
| /db/_index                                   | POST   | :hourglass_flowing_sand: |
| /db/_index                                   | GET    | :hourglass_flowing_sand: |
| /db/_index                                   | DELETE | :hourglass_flowing_sand: |
| /db/_compact/design-doc                      | POST   | :hourglass_flowing_sand: |
| /db/_view_cleanup                            | POST   | :hourglass_flowing_sand: |

### Document

| Endpoint                                     | Method | Status                   |
|----------------------------------------------|--------|--------------------------|
| /db                                          | POST   | :heavy_check_mark:       |
| /db/doc                                      | HEAD   | :heavy_check_mark:       |
| /db/doc                                      | GET    | :heavy_check_mark:       |
| /db/doc                                      | PUT    | :heavy_check_mark:       |
| /db/doc                                      | DELETE | :heavy_check_mark:       |
| /db/doc                                      | COPY   | :heavy_multiplication_x: |

### Attachment
| Endpoint                                     | Method | Status                   |
|----------------------------------------------|--------|--------------------------|
| /db/doc/attachment                           | HEAD   | :heavy_check_mark:       |
| /db/doc/attachment                           | GET    | :hourglass_flowing_sand: |
| /db/doc/attachment                           | PUT    | :hourglass_flowing_sand: |
| /db/doc/attachment                           | DELETE | :heavy_check_mark:       |

#### Design
*Pending*

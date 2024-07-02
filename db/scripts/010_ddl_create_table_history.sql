CREATE TABLE history (
    id SERIAL PRIMARY KEY,
    start_at TIMESTAMP,
    end_at TIMESTAMP,
    car_id int not null REFERENCES cars(id)
)
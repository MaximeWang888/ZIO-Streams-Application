-- docker/init.sql
CREATE TABLE Coordinates (
                             latitude DOUBLE NOT NULL,
                             longitude DOUBLE NOT NULL,
                             PRIMARY KEY (latitude, longitude)
);

CREATE TABLE City (
                      name VARCHAR(255) NOT NULL,
                      latitude DOUBLE NOT NULL,
                      longitude DOUBLE NOT NULL,
                      PRIMARY KEY (name),
                      FOREIGN KEY (latitude, longitude) REFERENCES Coordinates(latitude, longitude)
);

CREATE TABLE CurrentWeather (
                                latitude DOUBLE NOT NULL,
                                longitude DOUBLE NOT NULL,
                                temperature DOUBLE NOT NULL,
                                weatherDescription VARCHAR(255) NOT NULL,
                                date VARCHAR(255) NOT NULL,
                                PRIMARY KEY (latitude, longitude),
                                FOREIGN KEY (latitude, longitude) REFERENCES Coordinates(latitude, longitude)
);

CREATE TABLE TemperatureRecommendation (
                                           temperatureRange VARCHAR(50) NOT NULL,
                                           minTemperature DOUBLE,
                                           maxTemperature DOUBLE,
                                           recommendation VARCHAR(255) NOT NULL,
                                           PRIMARY KEY (temperatureRange)
);



INSERT INTO Coordinates VALUES (40.7128, -74.0060); -- New York
INSERT INTO Coordinates VALUES (34.0522, -118.2437); -- Los Angeles
INSERT INTO Coordinates VALUES (37.7749, -122.4194); -- San Francisco
INSERT INTO Coordinates VALUES (41.8781, -87.6298); -- Chicago
INSERT INTO Coordinates VALUES (51.5099, -0.1180);  -- London
INSERT INTO Coordinates VALUES (4.7128, -74.0060);  -- ??
INSERT INTO Coordinates VALUES (3.0522, -118.2437);  -- ??


INSERT INTO City VALUES ('NewYork', 40.7128, -74.0060);
INSERT INTO City VALUES ('SanFrancisco', 37.7749, -122.4194);
INSERT INTO City VALUES ('Chicago', 41.8781, -87.6298);
INSERT INTO City VALUES ('London', 51.5099, -0.1180);
INSERT INTO City VALUES ('LosAngeles', 34.0522, -118.2437);
INSERT INTO City VALUES ('Paris', 4.7128, -74.0060);
INSERT INTO City VALUES ('Madrid', 3.0522, -118.2437);


INSERT INTO CurrentWeather VALUES (40.7128, -74.0060, 25.0, 'Sunny', '2024-01-05');
INSERT INTO CurrentWeather VALUES (34.0522, -118.2437, 18.5, 'Cloudy', '2024-01-05');
INSERT INTO CurrentWeather VALUES (37.7749, -122.4194, 22.5, 'PartlyCloudy', '2024-01-06');
INSERT INTO CurrentWeather VALUES (41.8781, -87.6298, 15.0, 'Rainy', '2024-01-06');
INSERT INTO CurrentWeather VALUES (51.5099, -0.1180, 10.0, 'Cloudy', '2024-01-06');
INSERT INTO CurrentWeather VALUES (4.7128, -74.0060, 28.0, 'Sunny', '2024-01-06');
INSERT INTO CurrentWeather VALUES (3.0522, -118.2437, 20.5, 'Clear', '2024-01-06');


INSERT INTO TemperatureRecommendation VALUES ('Inferieur ou egal a 5', null, 5, 'Portez des vetements chauds et une echarpe.');
INSERT INTO TemperatureRecommendation VALUES ('Entre 5 et 20', 5, 20, 'Vous pouvez porter un pull leger.');
INSERT INTO TemperatureRecommendation VALUES ('Entre 20 et 30', 20, 30, 'Optez pour des vetements legers et confortables.');
INSERT INTO TemperatureRecommendation VALUES ('Superieur a 30', 30, null, 'Portez des vetements legers et assurez-vous de rester hydrate.');

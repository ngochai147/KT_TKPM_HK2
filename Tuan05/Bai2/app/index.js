const express = require('express');
const mysql = require('mysql2');
const redis = require('redis');

const app = express();

// MySQL (pool)
const db = mysql.createPool({
  host: process.env.DB_HOST || 'db',
  user: 'root',
  password: process.env.MYSQL_ROOT_PASSWORD,
  database: process.env.MYSQL_DATABASE,
  port: 3306
});

// Redis
const redisClient = redis.createClient({
  url: `redis://${process.env.REDIS_HOST || 'redis'}:6379`
});

redisClient.connect();

app.get('/', async (req, res) => {
  try {
    const [rows] = await db.promise().query('SELECT NOW() as time');

    await redisClient.set('last_time', new Date(rows[0].time).toISOString());
    const cached = await redisClient.get('last_time');

    res.send({
      mysql_time: rows[0].time,
      redis_cache: cached
    });
  } catch (err) {
    console.error(err);
    res.status(500).send(err.message);
  }
});

app.listen(3000, () => {
  console.log('App running on port 3000');
});
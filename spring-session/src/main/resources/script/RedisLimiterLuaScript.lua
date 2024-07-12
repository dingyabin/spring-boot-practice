local key = KEYS[1]
local limit = tonumber(ARGV[1])
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then
    return false
else
    redis.call('INCR', key)
    redis.call('EXPIRE', key, ARGV[2])
    return true
end
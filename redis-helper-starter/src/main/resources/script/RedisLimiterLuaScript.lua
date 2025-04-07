local key = KEYS[1]
local count = redis.call('INCR', key)
if count == 1 then
    redis.call('EXPIRE', key, ARGV[1])
end
return count
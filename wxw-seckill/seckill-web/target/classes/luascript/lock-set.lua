--- 获取key
local key = KEYS[1]
--- 获取value
local val = KEYS[2]
--- 获取一个参数
local expire = ARGV[1]
--- 如果redis找不到这个key就去插入
if redis.call("get", key) == false then
    --- 如果插入成功，就去设置过期值
    if redis.call("set", key, val) then
        --- 由于lua脚本接收到参数都会转为String，所以要转成数字类型才能比较
        if tonumber(expire) > 0 then
            --- 设置过期时间
            redis.call("expire", key, expire)
        end
        return true
    end
    return false
else
    return false
end
local userId = KEYS[1]
local skuInfo = KEYS[2]
local oid = KEYS[3]
local snapped = "snapped:" .. skuInfo;
local secKey = "commodity:" .. skuInfo
local orderKey = "SEC_OID:" .. oid
-- 恢复sku数量
redis.call("incr", secKey)
-- 清除秒杀成功的记录
redis.call("del", orderKey)
redis.call("srem", snapped, userId)
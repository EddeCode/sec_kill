-- 1 先查看是否秒杀成功
-- 2 查看是否有货存
-- 3 判断是否秒杀成功 成功则减少货存
local userid=KEYS[1];
local skuInfo=KEYS[2];
local qtkey="commodity:"..skuInfo;
local usersKey="snapped:"..skuInfo;
local userExists=redis.call("sismember",usersKey,userid);
if tonumber(userExists)==1 then
  return 2;
end
local num= redis.call("get" ,qtkey);
if tonumber(num)<=0 then
  return 0;
else
  redis.call("decr",qtkey);
  redis.call("sadd",usersKey,userid);
end
return 1;
-- 0: no more stock
-- 1: success
-- 2: user already snapped

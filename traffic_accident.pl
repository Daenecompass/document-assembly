% RULES

% The Criminal Code, article 289 paragraph 1
committed(Defendant,art289par1) :- traffic_violation(Defendant), body_injuries(Victim, minor).
committed(Defendant,art289par1) :- traffic_violation(Defendant), property_damage(Value), Value>200000.
% The Criminal Code, article 289 paragraph 3
committed(Defendant,art289par3) :- committed(Defendant,art289par1), negligence(Defendant).

% The Criminal Code, article 297 paragraph 1
committed(Defendant,art297par1) :- traffic_violation(Defendant), body_injuries(Victim, grievous).
% The Criminal Code, article 297 paragraph 1, Bulletin of the Supreme Court of Serbia, 1/2006 and 4/2008
committed(Defendant,art297par1) :- traffic_violation(Defendant), property_damage(Value), Value>6000000.

% The Criminal Code, article 297 paragraph 2
committed(Defendant,art297par2) :- traffic_violation(Defendant), body_injuries(Victim, death).
% The Criminal Code, article 297 paragraph 3
committed(Defendant,art297par3) :- committed(Defendant,art297par1), negligence(Defendant). 

% The Law on Road Traffic Safety, article 82
traffic_violation(Defendant) :- unsafe_distance(Defendant). % zobsnp cl82
% The Law on Road Traffic Safety, article 187 paragraph 2
traffic_violation(Defendant) :- high_alcohol_level(Defendant).
% The Law on Road Traffic Safety, article 43
traffic_violation(Defendant) :- excessive_speed_in_populated_area(Defendant).
excessive_speed_in_populated_area(Defendant) :- speed(Defendant,Speed), Speed > 50.
% The Law on Road Traffic Safety, article 187 paragraph 3
high_alcohol_level(Defendant) :- alcohol_level(Defendant,X), X > 0.3.


jurisdiction(Offense, Location, Court) :- offense_severity(Offense, Severity), territorial_jurisdiction(Severity, Location, Court).
    
% The Law on Organization of Courts, article 22
offense_severity(Offense, light) :- imprisonment(Offense, X), X =< 10.
% The Law on Organization of Courts, article 23
offense_severity(Offense, serious) :- imprisonment(Offense, X), X > 10.

% The Law on Seat and Territorial Jurisdiction of Courts and Public Prosecutors' Offices, article 3
territorial_jurisdiction(light, 'Bac', 'Osnovni sud u Backoj Palanci').
% The Law on Seat and Territorial Jurisdiction of Courts and Public Prosecutors' Offices, article 4
territorial_jurisdiction(serious, 'Bac', 'Visi sud u Novom Sadu').

% The Criminal Code, article 289 paragraph 1
imprisonment(art289par1,3).
% The Criminal Code, article 289 paragraph 3
imprisonment(art289par3,1).
% The Criminal Code, article 297 paragraph 1
imprisonment(art297par1,8).
% The Criminal Code, article 297 paragraph 2
imprisonment(art297par2,12).
% The Criminal Code, article 297 paragraph 3
imprisonment(art297par3,4).

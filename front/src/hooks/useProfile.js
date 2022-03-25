import { useState } from "react";

export default function useProfile() {
  const [profile, setProfile] = useState(JSON.parse(localStorage.getItem("profile")));

  const saveProfile = (jsonProfile) => {
    localStorage.setItem("profile", JSON.stringify(jsonProfile));
    setProfile(jsonProfile);
  };

  return {
    setProfile: saveProfile,
    profile: profile
  };
}